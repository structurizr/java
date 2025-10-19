package com.structurizr.dsl;

import com.structurizr.PropertyHolder;
import com.structurizr.Workspace;
import com.structurizr.http.HttpClient;
import com.structurizr.model.*;
import com.structurizr.util.FeatureNotEnabledException;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main DSL parser class - forms the API for using the parser.
 */
public final class StructurizrDslParser extends StructurizrDslTokens {

    private static final Log log = LogFactory.getLog(StructurizrDslParser.class);

    private static final String BOM = "\uFEFF";

    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*");

    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*?(//|#).*$");
    private static final String MULTI_LINE_COMMENT_START_TOKEN = "/*";
    private static final String MULTI_LINE_COMMENT_END_TOKEN = "*/";
    private static final String MULTI_LINE_SEPARATOR = "\\";
    private static final String TEXT_BLOCK_MARKER = "\"\"\"";

    private static final Pattern STRING_SUBSTITUTION_PATTERN = Pattern.compile("(\\$\\{[a-zA-Z0-9-_.]+?})");
    private static final String STRING_SUBSTITUTION_TEMPLATE = "${%s}";

    private static final String STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME = "structurizr.dsl.identifier";

    private Charset characterEncoding = StandardCharsets.UTF_8;
    private IdentifierScope identifierScope = IdentifierScope.Flat;
    private final Stack<DslContext> contextStack;
    private final Set<String> parsedTokens = new HashSet<>();
    private final IdentifiersRegister identifiersRegister;
    private Map<String, NameValuePair> constantsAndVariables;
    private Features features = new Features();
    private HttpClient httpClient = new HttpClient();

    private Map<String,Map<String,Archetype>> archetypes = Map.of(
            StructurizrDslTokens.GROUP_TOKEN, new HashMap<>(),
            StructurizrDslTokens.CUSTOM_ELEMENT_TOKEN, new HashMap<>(),
            StructurizrDslTokens.PERSON_TOKEN, new HashMap<>(),
            StructurizrDslTokens.SOFTWARE_SYSTEM_TOKEN, new HashMap<>(),
            StructurizrDslTokens.CONTAINER_TOKEN, new HashMap<>(),
            StructurizrDslTokens.COMPONENT_TOKEN, new HashMap<>(),
            StructurizrDslTokens.DEPLOYMENT_NODE_TOKEN, new HashMap<>(),
            StructurizrDslTokens.INFRASTRUCTURE_NODE_TOKEN, new HashMap<>(),
            StructurizrDslTokens.RELATIONSHIP_TOKEN, new HashMap<>()
    );

    private boolean dslPortable = true;
    private final List<String> dslSourceLines = new ArrayList<>();
    private Workspace workspace;
    private boolean extendingWorkspace = false;

    /**
     * Creates a new instance of the parser.
     */
    public StructurizrDslParser() {
        contextStack = new Stack<>();
        identifiersRegister = new IdentifiersRegister();
        constantsAndVariables = new HashMap<>();

        features.enable(Features.ENVIRONMENT);
        features.enable(Features.FILE_SYSTEM);
        features.enable(Features.HTTP);
        features.enable(Features.HTTPS);

        features.enable(Features.PLUGINS);
        features.enable(Features.SCRIPTS);
        features.enable(Features.COMPONENT_FINDER);

        features.enable(Features.DOCUMENTATION);
        features.enable(Features.DECISIONS);

        features.enable(Features.INCLUDE);
    }

    void configureFrom(StructurizrDslParser parser) {
        setIdentifierScope(parser.getIdentifierScope());
        archetypes = parser.archetypes;
        constantsAndVariables = parser.constantsAndVariables;
    }

    /**
     * Provides a way to change the character encoding used by the DSL parser.
     *
     * @param characterEncoding     a Charset instance
     */
    public void setCharacterEncoding(Charset characterEncoding) {
        if (characterEncoding == null) {
            throw new IllegalArgumentException("A character encoding must be specified");
        }

        this.characterEncoding = characterEncoding;
    }

    IdentifierScope getIdentifierScope() {
        return identifierScope;
    }

    private void setIdentifierScope(IdentifierScope identifierScope) {
        if (identifierScope == null) {
            identifierScope = IdentifierScope.Flat;
        }

        this.identifierScope = identifierScope;
        this.identifiersRegister.setIdentifierScope(identifierScope);
    }

    /**
     * Sets whether to run this parser in restricted mode (this stops !include, !docs, !adrs from working).
     *
     * @param restricted        true for restricted mode, false otherwise
     */
    @Deprecated
    public void setRestricted(boolean restricted) {
        features.configure(Features.ENVIRONMENT, !restricted);
        features.configure(Features.FILE_SYSTEM, !restricted);

        features.configure(Features.PLUGINS, !restricted);
        features.configure(Features.SCRIPTS, !restricted);
        features.configure(Features.COMPONENT_FINDER, !restricted);

        features.configure(Features.DOCUMENTATION, !restricted);
        features.configure(Features.DECISIONS, !restricted);
    }

    /**
     * Gets the workspace that has been created by parsing the Structurizr DSL.
     *
     * @return  a Workspace instance
     */
    public Workspace getWorkspace() {
        if (workspace != null) {
            if (dslPortable) {
                String value = workspace.getProperties().get(DslUtils.STRUCTURIZR_DSL_RETAIN_SOURCE_PROPERTY_NAME);
                if (value == null || value.equalsIgnoreCase("true")) {
                    DslUtils.setDsl(workspace, getParsedDsl());
                }
            }
        }

        return workspace;
    }

    private String getParsedDsl() {
        return String.join(System.lineSeparator(), dslSourceLines);
    }

    void parse(DslParserContext context, File path) throws StructurizrDslParserException {
        parse(path);

        context.copyFrom(identifiersRegister);
    }

    /**
     * Parses the specified Structurizr DSL file.
     *
     * @param dslFile       a File object representing a DSL file
     * @throws StructurizrDslParserException when something goes wrong
     */
    public void parse(File dslFile) throws StructurizrDslParserException {
        if (dslFile == null) {
            throw new StructurizrDslParserException("A file must be specified");
        }

        if (!dslFile.exists()) {
            throw new StructurizrDslParserException("The file at " + dslFile.getAbsolutePath() + " does not exist");
        }

        try {
            parse(Files.readAllLines(dslFile.toPath(), characterEncoding), dslFile, false, true);
        } catch (IOException e) {
            throw new StructurizrDslParserException(e.getMessage());
        }
    }

    void parse(DslParserContext context, String dsl) throws StructurizrDslParserException {
        parse(dsl);

        context.copyFrom(identifiersRegister);
    }

    /**
     * Parses the specified Structurizr DSL, adding the parsed content to the workspace.
     *
     * @param dsl       a Structurizr DSL definition, as a single String
     * @throws StructurizrDslParserException when something goes wrong
     */
    public void parse(String dsl) throws StructurizrDslParserException {
        parse(dsl, new File("."));
    }

    /**
     * Parses the specified Structurizr DSL, adding the parsed content to the workspace.
     *
     * @param dsl       a Structurizr DSL definition, as a single String
     * @param dslFile   a File representing the DSL file, and therefore where includes/images/etc should be loaded relative to
     * @throws StructurizrDslParserException when something goes wrong
     */
    public void parse(String dsl, File dslFile) throws StructurizrDslParserException {
        if (StringUtils.isNullOrEmpty(dsl)) {
            throw new StructurizrDslParserException("A DSL fragment must be specified");
        }

        List<String> lines = Arrays.asList(dsl.split("\\r?\\n"));
        parse(lines, dslFile, false, true);
    }

    void parse(List<String> lines, DslContext dslContext) throws StructurizrDslParserException {
        startContext(dslContext);
        parse(lines, null, true, false);
        endContext();
    }

    /**
     * Parses a list of Structurizr DSL lines.
     *
     * @param lines     a Structurizr DSL definition, as a List of String objects (one per line)
     * @param dslFile   a File representing the DSL file, and therefore where includes/images/etc should be loaded relative to
     * @throws StructurizrDslParserException when something goes wrong
     */
    void parse(List<String> lines, File dslFile, boolean fragment, boolean includeInDslSourceLines) throws StructurizrDslParserException {
        if (includeInDslSourceLines) {
            dslSourceLines.addAll(lines);
        }

        List<DslLine> dslLines = preProcessLines(lines);

        for (DslLine dslLine : dslLines) {
            String line = dslLine.getSource();

            if (line.startsWith(BOM)) {
                // this caters for files encoded as "UTF-8 with BOM"
                line = line.substring(1);
            }

            try {
                if (EMPTY_LINE_PATTERN.matcher(line).matches()) {
                    // do nothing
                } else if (COMMENT_PATTERN.matcher(line).matches()) {
                    // do nothing
                } else if (inContext(InlineScriptDslContext.class)) {
                    if (DslContext.CONTEXT_END_TOKEN.equals(line.trim())) {
                        endContext();
                    } else {
                        getContext(InlineScriptDslContext.class).addLine(line);
                    }
                } else {
                    List<String> listOfTokens = new Tokenizer().tokenize(line);
                    listOfTokens = listOfTokens.stream().map(this::substituteStrings).collect(Collectors.toList());

                    Tokens tokens = new Tokens(listOfTokens);

                    String identifier = null;
                    if (tokens.size() >= 3 && ASSIGNMENT_OPERATOR_TOKEN.equals(tokens.get(1))) {
                        identifier = tokens.get(0);
                        identifiersRegister.validateIdentifierName(identifier);

                        tokens = new Tokens(listOfTokens.subList(2, listOfTokens.size()));
                    }

                    String firstToken = tokens.get(0);

                    if (line.trim().startsWith(MULTI_LINE_COMMENT_START_TOKEN) && line.trim().endsWith(MULTI_LINE_COMMENT_END_TOKEN)) {
                        // do nothing
                    } else if (firstToken.startsWith(MULTI_LINE_COMMENT_START_TOKEN)) {
                        startContext(new CommentDslContext());

                    } else if (inContext(CommentDslContext.class) && line.trim().endsWith(MULTI_LINE_COMMENT_END_TOKEN)) {
                        endContext();

                    } else if (inContext(CommentDslContext.class)) {
                        // do nothing

                    } else if (DslContext.CONTEXT_END_TOKEN.equals(tokens.get(0))) {
                        endContext();

                    } else if (INCLUDE_FILE_TOKEN.equalsIgnoreCase(firstToken)) {
                        String leadingSpace = line.substring(0, line.indexOf(INCLUDE_FILE_TOKEN));

                        List<IncludedFile> files = new IncludeParser().parse(getContext(), dslFile, tokens);
                        for (IncludedFile includedFile : files) {
                            List<String> paddedLines = new ArrayList<>();
                            for (String unpaddedLine : includedFile.getLines()) {
                                if (unpaddedLine.startsWith(BOM)) {
                                    // this caters for files encoded as "UTF-8 with BOM"
                                    unpaddedLine = unpaddedLine.substring(1);
                                }
                                paddedLines.add(leadingSpace + unpaddedLine);
                            }

                            parse(paddedLines, includedFile.getFile(), true, false);
                        }

                    } else if (PLUGIN_TOKEN.equalsIgnoreCase(firstToken)) {
                        if (features.isEnabled(Features.PLUGINS)) {
                            String fullyQualifiedClassName = new PluginParser().parse(getContext(), tokens.withoutContextStartToken());
                            startContext(new PluginDslContext(fullyQualifiedClassName, dslFile, this));
                            if (!shouldStartContext(tokens)) {
                                // run the plugin immediately, without looking for parameters
                                endContext();
                            }
                        } else {
                            throw new FeatureNotEnabledException(Features.PLUGINS, firstToken + " is not permitted");
                        }

                    } else if (inContext(PluginDslContext.class)) {
                        new PluginParser().parseParameter(getContext(PluginDslContext.class), tokens);

                    } else if (SCRIPT_TOKEN.equalsIgnoreCase(firstToken)) {
                        if (features.isEnabled(Features.SCRIPTS)) {
                            ScriptParser scriptParser = new ScriptParser();
                            if (scriptParser.isInlineScript(tokens)) {
                                String language = scriptParser.parseInline(tokens.withoutContextStartToken());
                                startContext(new InlineScriptDslContext(getContext(), dslFile, this, language));
                            } else {
                                String filename = scriptParser.parseExternal(tokens.withoutContextStartToken());
                                startContext(new ExternalScriptDslContext(getContext(), dslFile, this, filename));

                                if (shouldStartContext(tokens)) {
                                    // we'll wait for parameters before executing the script
                                } else {
                                    endContext();
                                }
                            }
                        } else {
                            throw new FeatureNotEnabledException(Features.SCRIPTS, firstToken + " is not permitted");
                        }

                    } else if (inContext(ExternalScriptDslContext.class)) {
                        new ScriptParser().parseParameter(getContext(ExternalScriptDslContext.class), tokens);

                    } else if (tokens.size() >= 4 && tokens.get(1).equals(NO_RELATIONSHIP_TOKEN) && shouldStartContext(tokens) && inContext(DeploymentEnvironmentDslContext.class)) {
                        // source -/> destination {
                        // or
                        // source -/> destination "description" {

                        // remove source -> destination (between instances) in the deployment model
                        Set<Relationship> relationships = new NoRelationshipParser().parse(getContext(DeploymentEnvironmentDslContext.class), tokens.withoutContextStartToken());

                        // find the static element -> static element relationship that the removed relationships were based upon
                        Relationship relationship = workspace.getModel().getRelationship(relationships.iterator().next().getLinkedRelationshipId());

                        startContext(new NoRelationshipInDeploymentEnvironmentDslContext(getContext(DeploymentEnvironmentDslContext.class), relationship));

                    } else if (tokens.size() > 2 && isRelationshipKeywordOrArchetype(tokens.get(1)) && (inContext(ModelDslContext.class) || inContext(DeploymentEnvironmentDslContext.class) || inContext(ElementDslContext.class))) {
                        // explicit without archetype: a -> b
                        // explicit with archetype: a --https-> b
                        Archetype archetype = getArchetype(RELATIONSHIP_TOKEN, tokens.get(1));
                        Set<Relationship> relationships = new ExplicitRelationshipParser().parse(getContext(), tokens.withoutContextStartToken(), archetype);

                        if (relationships.size() == 1) {
                            Relationship relationship = relationships.iterator().next();
                            registerIdentifier(identifier, relationship);

                            if (shouldStartContext(tokens)) {
                                startContext(new RelationshipDslContext(relationship));
                            }
                        } else {
                            if (shouldStartContext(tokens)) {
                                startContext(new RelationshipsDslContext(getContext(), relationships));
                            }
                        }

                    } else if (tokens.size() >= 2 && isRelationshipKeywordOrArchetype(tokens.get(0)) && inContext(ElementDslContext.class)) {
                        // implicit without archetype: -> this
                        // implicit with archetype: --https-> this
                        Archetype archetype = getArchetype(RELATIONSHIP_TOKEN, tokens.get(0));
                        Set<Relationship> relationships = new ImplicitRelationshipParser().parse(getContext(ElementDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (relationships.size() == 1) {
                            Relationship relationship = relationships.iterator().next();
                            registerIdentifier(identifier, relationship);

                            if (shouldStartContext(tokens)) {
                                startContext(new RelationshipDslContext(relationship));
                            }
                        } else {
                            if (shouldStartContext(tokens)) {
                                startContext(new RelationshipsDslContext(getContext(), relationships));
                            }
                        }

                    } else if (tokens.size() > 2 && isRelationshipKeywordOrArchetype(tokens.get(1)) && inContext(ElementsDslContext.class)) {
                        Archetype archetype = getArchetype(RELATIONSHIP_TOKEN, tokens.get(1));
                        Set<Relationship> relationships = new ExplicitRelationshipParser().parse(getContext(ElementsDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipsDslContext(getContext(), relationships));
                        }

                    } else if (tokens.size() >= 2 && isRelationshipKeywordOrArchetype(tokens.get(0)) && inContext(ElementsDslContext.class)) {
                        Archetype archetype = getArchetype(RELATIONSHIP_TOKEN, tokens.get(1));
                        Set<Relationship> relationships = new ImplicitRelationshipParser().parse(getContext(ElementsDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipsDslContext(getContext(), relationships));
                        }

                    } else if ((FIND_ELEMENT_TOKEN.equalsIgnoreCase(firstToken) || FIND_RELATIONSHIP_TOKEN.equalsIgnoreCase(firstToken) || REF_TOKEN.equalsIgnoreCase(firstToken) || EXTEND_TOKEN.equalsIgnoreCase(firstToken)) && (inContext(ModelItemDslContext.class) || inContext(ModelDslContext.class))) {
                        ModelItem modelItem = null;

                        if (REF_TOKEN.equalsIgnoreCase(firstToken)) {
                            throw new RuntimeException(REF_TOKEN + " was previously deprecated, and has now been removed - please use " + FIND_ELEMENT_TOKEN + " or " + FIND_RELATIONSHIP_TOKEN + " instead");
                        } else if (EXTEND_TOKEN.equalsIgnoreCase(firstToken)) {
                            throw new RuntimeException(EXTEND_TOKEN + " was previously deprecated, and has now been removed - please use " + FIND_ELEMENT_TOKEN + " or " + FIND_RELATIONSHIP_TOKEN + " instead");
                        } else if (FIND_ELEMENT_TOKEN.equalsIgnoreCase(firstToken)) {
                            modelItem = new FindElementParser().parse(getContext(), tokens.withoutContextStartToken());
                        } else if (FIND_RELATIONSHIP_TOKEN.equalsIgnoreCase(firstToken)) {
                            modelItem = new FindRelationshipParser().parse(getContext(), tokens.withoutContextStartToken());
                        }

                        if (shouldStartContext(tokens)) {
                            if (modelItem instanceof Person) {
                                startContext(new PersonDslContext((Person)modelItem));
                            } else if (modelItem instanceof SoftwareSystem) {
                                startContext(new SoftwareSystemDslContext((SoftwareSystem)modelItem));
                            } else if (modelItem instanceof Container) {
                                startContext(new ContainerDslContext((Container) modelItem));
                            } else if (modelItem instanceof Component) {
                                startContext(new ComponentDslContext((Component)modelItem));
                            } else if (modelItem instanceof DeploymentEnvironment) {
                                startContext(new DeploymentEnvironmentDslContext(((DeploymentEnvironment)modelItem).getName()));
                            } else if (modelItem instanceof DeploymentNode) {
                                startContext(new DeploymentNodeDslContext((DeploymentNode)modelItem));
                            } else if (modelItem instanceof InfrastructureNode) {
                                startContext(new InfrastructureNodeDslContext((InfrastructureNode)modelItem));
                            } else if (modelItem instanceof SoftwareSystemInstance) {
                                startContext(new SoftwareSystemInstanceDslContext((SoftwareSystemInstance)modelItem));
                            } else if (modelItem instanceof ContainerInstance) {
                                startContext(new ContainerInstanceDslContext((ContainerInstance)modelItem));
                            } else if (modelItem instanceof Relationship) {
                                startContext(new RelationshipDslContext((Relationship)modelItem));
                            }
                        }

                        if (!StringUtils.isNullOrEmpty(identifier)) {
                            if (modelItem instanceof Element) {
                                registerIdentifier(identifier, (Element)modelItem);
                            } else if (modelItem instanceof Relationship) {
                                registerIdentifier(identifier, (Relationship)modelItem);
                            }
                        }

                    } else if (FIND_ELEMENTS_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ModelDslContext.class) || inContext(DeploymentEnvironmentDslContext.class) || inContext(ElementDslContext.class))) {
                        Set<Element> elements = new FindElementsParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new ElementsDslContext(getContext(), elements));
                        }

                    } else if (FIND_RELATIONSHIPS_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ModelDslContext.class) || inContext(DeploymentEnvironmentDslContext.class) || inContext(ElementDslContext.class))) {
                        Set<Relationship> relationships = new FindRelationshipsParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipsDslContext(getContext(), relationships));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, CUSTOM_ELEMENT_TOKEN) && (inContext(ModelDslContext.class))) {
                        Archetype archetype = getArchetype(CUSTOM_ELEMENT_TOKEN, firstToken);
                        CustomElement customElement = new CustomElementParser().parse(getContext(ModelDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new CustomElementDslContext(customElement));
                        }

                        registerIdentifier(identifier, customElement);

                    } else if (isElementKeywordOrArchetype(firstToken, PERSON_TOKEN) && (inContext(ModelDslContext.class))) {
                        Archetype archetype = getArchetype(PERSON_TOKEN, firstToken);
                        Person person = new PersonParser().parse(getContext(ModelDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new PersonDslContext(person));
                        }

                        registerIdentifier(identifier, person);

                    } else if (isElementKeywordOrArchetype(firstToken, SOFTWARE_SYSTEM_TOKEN) && (inContext(ModelDslContext.class))) {
                        Archetype archetype = getArchetype(SOFTWARE_SYSTEM_TOKEN, firstToken);
                        SoftwareSystem softwareSystem = new SoftwareSystemParser().parse(getContext(ModelDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new SoftwareSystemDslContext(softwareSystem));
                        }

                        registerIdentifier(identifier, softwareSystem);

                    } else if (isElementKeywordOrArchetype(firstToken, CONTAINER_TOKEN) && inContext(SoftwareSystemDslContext.class)) {
                        Archetype archetype = getArchetype(CONTAINER_TOKEN, firstToken);
                        Container container = new ContainerParser().parse(getContext(SoftwareSystemDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new ContainerDslContext(container));
                        }

                        registerIdentifier(identifier, container);

                    } else if (isElementKeywordOrArchetype(firstToken, COMPONENT_TOKEN) && inContext(ContainerDslContext.class)) {
                        Archetype archetype = getArchetype(COMPONENT_TOKEN, firstToken);
                        Component component = new ComponentParser().parse(getContext(ContainerDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new ComponentDslContext(component));
                        }

                        registerIdentifier(identifier, component);

                    } else if (COMPONENT_FINDER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        if (features.isEnabled(Features.COMPONENT_FINDER)) {
                            if (shouldStartContext(tokens)) {
                                startContext(new ComponentFinderDslContext(this, getContext(ContainerDslContext.class)));
                            }
                        } else {
                            throw new FeatureNotEnabledException(Features.COMPONENT_FINDER, firstToken + " is not permitted");
                        }

                    } else if (COMPONENT_FINDER_CLASSES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderDslContext.class)) {
                        new ComponentFinderParser().parseClasses(getContext(ComponentFinderDslContext.class), tokens);

                    } else if (COMPONENT_FINDER_SOURCE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderDslContext.class)) {
                        new ComponentFinderParser().parseSource(getContext(ComponentFinderDslContext.class), tokens);

                    } else if (COMPONENT_FINDER_FILTER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderDslContext.class)) {
                        new ComponentFinderParser().parseFilter(getContext(ComponentFinderDslContext.class), tokens);

                    } else if (COMPONENT_FINDER_STRATEGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderDslContext.class)) {
                        if (shouldStartContext(tokens)) {
                            startContext(new ComponentFinderStrategyDslContext(getContext(ComponentFinderDslContext.class)));
                        }

                    } else if (COMPONENT_FINDER_STRATEGY_TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseTechnology(getContext(ComponentFinderStrategyDslContext.class), tokens);

                    } else if (COMPONENT_FINDER_STRATEGY_MATCHER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseMatcher(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_FILTER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseFilter(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_SUPPORTING_TYPES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseSupportingTypes(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_NAME_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseName(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseDescription(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        new ComponentFinderStrategyParser().parseUrl(getContext(ComponentFinderStrategyDslContext.class), tokens, dslFile);

                    } else if (COMPONENT_FINDER_STRATEGY_FOREACH_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentFinderStrategyDslContext.class)) {
                        if (shouldStartContext(tokens)) {
                            startContext(new ComponentFinderStrategyForEachDslContext(getContext(ComponentFinderStrategyDslContext.class), this));
                        }

                    } else if (inContext(ComponentFinderStrategyForEachDslContext.class)) {
                        getContext(ComponentFinderStrategyForEachDslContext.class).addLine(line);

                    } else if (ENTERPRISE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        throw new RuntimeException("The enterprise keyword was previously deprecated, and has now been removed - please use group instead (https://docs.structurizr.com/dsl/language#group)");

                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(ModelDslContext.class)) {
                        ElementGroup group = new GroupParser().parseContext(getContext(ModelDslContext.class), tokens);

                        startContext(new ModelDslContext(group));
                        registerIdentifier(identifier, group);
                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(SoftwareSystemDslContext.class)) {
                        ElementGroup group = new GroupParser().parseContext(getContext(SoftwareSystemDslContext.class), tokens);

                        SoftwareSystem softwareSystem = getContext(SoftwareSystemDslContext.class).getSoftwareSystem();
                        group.setParent(softwareSystem);
                        startContext(new SoftwareSystemDslContext(softwareSystem, group));
                        registerIdentifier(identifier, group);
                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(ContainerDslContext.class)) {
                        ElementGroup group = new GroupParser().parseContext(getContext(ContainerDslContext.class), tokens);

                        Container container = getContext(ContainerDslContext.class).getContainer();
                        group.setParent(container);
                        startContext(new ContainerDslContext(container, group));
                        registerIdentifier(identifier, group);
                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(DeploymentEnvironmentDslContext.class)) {
                        ElementGroup group = new GroupParser().parseContext(getContext(DeploymentEnvironmentDslContext.class), tokens);

                        DeploymentEnvironment environment = getContext(DeploymentEnvironmentDslContext.class).getEnvironment();
                        startContext(new DeploymentEnvironmentDslContext(environment.getName(), group));
                        registerIdentifier(identifier, group);
                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(DeploymentNodeDslContext.class)) {
                        ElementGroup group = new GroupParser().parseContext(getContext(DeploymentNodeDslContext.class), tokens);

                        DeploymentNode deploymentNode = getContext(DeploymentNodeDslContext.class).getDeploymentNode();
                        startContext(new DeploymentNodeDslContext(deploymentNode, group));
                        registerIdentifier(identifier, group);
                    } else if ((TAGS_TOKEN.equalsIgnoreCase(firstToken) || TAG_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ModelItemDslContext.class) && !isGroup(getContext())) {
                        new ModelItemParser().parseTags(getContext(ModelItemDslContext.class), tokens);

                    } else if ((TAGS_TOKEN.equalsIgnoreCase(firstToken) || TAG_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ModelItemsDslContext.class)) {
                        new ModelItemsParser().parseTags(getContext(ModelItemsDslContext.class), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementDslContext.class) && !isGroup(getContext())) {
                        new ModelItemParser().parseDescription(getContext(ElementDslContext.class), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementsDslContext.class)) {
                        new ElementsParser().parseDescription(getContext(ElementsDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class) && !getContext(ContainerDslContext.class).hasGroup()) {
                        new ContainerParser().parseTechnology(getContext(ContainerDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class) && !getContext(ComponentDslContext.class).hasGroup()) {
                        new ComponentParser().parseTechnology(getContext(ComponentDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        new DeploymentNodeParser().parseTechnology(getContext(DeploymentNodeDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(InfrastructureNodeDslContext.class)) {
                        new InfrastructureNodeParser().parseTechnology(getContext(InfrastructureNodeDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementsDslContext.class)) {
                        new ElementsParser().parseTechnology(getContext(ElementsDslContext.class), tokens);

                    } else if (INSTANCES_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        new DeploymentNodeParser().parseInstances(getContext(DeploymentNodeDslContext.class), tokens);

                    } else if (URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !isGroup(getContext())) {
                        new ModelItemParser().parseUrl(getContext(ModelItemDslContext.class), tokens);

                    } else if (URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemsDslContext.class)) {
                        new ModelItemsParser().parseUrl(getContext(ModelItemsDslContext.class), tokens);

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace.getModel()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext(ConfigurationDslContext.class).getWorkspace()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !isGroup(getContext())) {
                        startContext(new PropertiesDslContext(getContext(ModelItemDslContext.class).getModelItem()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemsDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext(ModelItemsDslContext.class).getModelItems().stream().map(mi -> (PropertyHolder)mi).toList()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new PropertiesDslContext(workspace.getViews().getConfiguration()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext(ViewDslContext.class).getView()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(DynamicViewRelationshipContext.class)) {
                        startContext(new PropertiesDslContext(getContext((DynamicViewRelationshipContext.class)).getRelationshipView()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext((ElementStyleDslContext.class)).getStyle()));

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        startContext(new PropertiesDslContext(getContext((RelationshipStyleDslContext.class)).getStyle()));

                    } else if (inContext(PropertiesDslContext.class)) {
                        new PropertyParser().parse(getContext(PropertiesDslContext.class), tokens);

                    } else if (PERSPECTIVES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemDslContext.class) && !isGroup(getContext())) {
                        startContext(new PerspectivesDslContext(getContext(ModelItemDslContext.class).getModelItem()));

                    } else if (PERSPECTIVES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelItemsDslContext.class)) {
                        startContext(new PerspectivesDslContext(getContext(ModelItemsDslContext.class).getModelItems()));

                    } else if (inContext(PerspectivesDslContext.class)) {
                        new PerspectiveParser().parse(getContext(PerspectivesDslContext.class), tokens);

                    } else if (GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class)) {
                        new GroupParser().parseProperty(getContext(ComponentDslContext.class), tokens);

                    } else if (WORKSPACE_TOKEN.equalsIgnoreCase(firstToken) && contextStack.empty()) {
                        if (parsedTokens.contains(WORKSPACE_TOKEN)) {
                            throw new RuntimeException("Multiple workspaces are not permitted in a DSL definition");
                        }
                        DslParserContext dslParserContext = new DslParserContext(this, dslFile);
                        dslParserContext.setIdentifierRegister(identifiersRegister);
                        dslParserContext.setFeatures(features);
                        dslParserContext.setHttpClient(httpClient);

                        workspace = new WorkspaceParser().parse(dslParserContext, tokens.withoutContextStartToken());
                        extendingWorkspace = !workspace.getModel().isEmpty();
                        WorkspaceDslContext context = new WorkspaceDslContext();
                        context.setDslPortable(dslParserContext.isDslPortable());
                        startContext(context);
                        parsedTokens.add(WORKSPACE_TOKEN);

                    } else if (IMPLIED_RELATIONSHIPS_TOKEN.equalsIgnoreCase(firstToken) || IMPLIED_RELATIONSHIPS_TOKEN.substring(1).equalsIgnoreCase(firstToken)) {
                        new ImpliedRelationshipsParser().parse(getContext(), tokens, dslFile);

                    } else if (NAME_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        new WorkspaceParser().parseName(getContext(), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        new WorkspaceParser().parseDescription(getContext(), tokens);

                    } else if (MODEL_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (parsedTokens.contains(MODEL_TOKEN)) {
                            throw new RuntimeException("Multiple models are not permitted in a DSL definition");
                        }

                        startContext(new ModelDslContext());
                        parsedTokens.add(MODEL_TOKEN);

                    } else if (ARCHETYPES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        startContext(new ArchetypesDslContext());

                    } else if (isElementKeywordOrArchetype(firstToken, GROUP_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, GROUP_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                    } else if (isElementKeywordOrArchetype(firstToken, CUSTOM_ELEMENT_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, CUSTOM_ELEMENT_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new CustomElementArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, PERSON_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, PERSON_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);


                        if (shouldStartContext(tokens)) {
                            startContext(new PersonArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, SOFTWARE_SYSTEM_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, SOFTWARE_SYSTEM_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new SoftwareSystemArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, CONTAINER_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, CONTAINER_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new ContainerArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, COMPONENT_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, COMPONENT_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new ComponentArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, DEPLOYMENT_NODE_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, DEPLOYMENT_NODE_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentNodeArchetypeDslContext(archetype));
                        }

                    } else if (isElementKeywordOrArchetype(firstToken, INFRASTRUCTURE_NODE_TOKEN) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, INFRASTRUCTURE_NODE_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new InfrastructureNodeArchetypeDslContext(archetype));
                        }

                    } else if (isRelationshipKeywordOrArchetype(firstToken) && inContext(ArchetypesDslContext.class)) {
                        Archetype archetype = new Archetype(identifier, RELATIONSHIP_TOKEN);
                        extendArchetype(archetype, firstToken);
                        addArchetype(archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new RelationshipArchetypeDslContext(archetype));
                        }

                    } else if (METADATA_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomElementArchetypeDslContext.class)) {
                        new ArchetypeParser().parseMetadata(getContext(ArchetypeDslContext.class), tokens);

                    } else if (DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ArchetypeDslContext.class)) {
                        new ArchetypeParser().parseDescription(getContext(ArchetypeDslContext.class), tokens);

                    } else if (TECHNOLOGY_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ContainerArchetypeDslContext.class) || inContext(ComponentArchetypeDslContext.class) || inContext(DeploymentNodeArchetypeDslContext.class) || inContext(InfrastructureNodeArchetypeDslContext.class) || inContext(RelationshipArchetypeDslContext.class))) {
                        new ArchetypeParser().parseTechnology(getContext(ArchetypeDslContext.class), tokens);

                    } else if (TAG_TOKEN.equalsIgnoreCase(firstToken) && inContext(ArchetypeDslContext.class)) {
                        new ArchetypeParser().parseTag(getContext(ArchetypeDslContext.class), tokens);

                    } else if (TAGS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ArchetypeDslContext.class)) {
                        new ArchetypeParser().parseTags(getContext(ArchetypeDslContext.class), tokens);

                    } else if (PROPERTIES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ArchetypeDslContext.class)) {
                        Archetype archetype = getContext(ArchetypeDslContext.class).getArchetype();
                        startContext(new PropertiesDslContext(archetype));

                    } else if (PERSPECTIVES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ArchetypeDslContext.class)) {
                        Archetype archetype = getContext(ArchetypeDslContext.class).getArchetype();
                        startContext(new PerspectivesDslContext(archetype));

                    } else if (VIEWS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (parsedTokens.contains(VIEWS_TOKEN)) {
                            throw new RuntimeException("Multiple view sets are not permitted in a DSL definition");
                        }

                        startContext(new ViewsDslContext());
                        parsedTokens.add(VIEWS_TOKEN);

                    } else if (BRANDING_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new BrandingDslContext(dslFile));

                    } else if (BRANDING_LOGO_TOKEN.equalsIgnoreCase(firstToken) && inContext(BrandingDslContext.class)) {
                        new BrandingParser().parseLogo(getContext(BrandingDslContext.class), tokens);

                    } else if (BRANDING_FONT_TOKEN.equalsIgnoreCase(firstToken) && inContext(BrandingDslContext.class)) {
                        new BrandingParser().parseFont(getContext(BrandingDslContext.class), tokens);

                    } else if (STYLES_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new StylesDslContext());

                    } else if (LIGHT_COLOR_SCHEME_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        startContext(new StylesDslContext(ColorScheme.Light));

                    } else if (DARK_COLOR_SCHEME_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        startContext(new StylesDslContext(ColorScheme.Dark));

                    } else if (ELEMENT_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        ElementStyle elementStyle = new ElementStyleParser().parseElementStyle(getContext(StylesDslContext.class), tokens.withoutContextStartToken());
                        startContext(new ElementStyleDslContext(elementStyle, dslFile));

                    } else if (ELEMENT_STYLE_BACKGROUND_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseBackground(getContext(ElementStyleDslContext.class), tokens);

                    } else if ((ELEMENT_STYLE_COLOUR_TOKEN.equalsIgnoreCase(firstToken) || ELEMENT_STYLE_COLOR_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseColour(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_STROKE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseStroke(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_STROKE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseStrokeWidth(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_SHAPE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseShape(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_BORDER_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseBorder(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_OPACITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseOpacity(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseWidth(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_HEIGHT_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseHeight(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_FONT_SIZE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseFontSize(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_METADATA_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseMetadata(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseDescription(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_ICON_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseIcon(getContext(ElementStyleDslContext.class), tokens);

                    } else if (ELEMENT_STYLE_ICON_POSITION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ElementStyleDslContext.class)) {
                        new ElementStyleParser().parseIconPosition(getContext(ElementStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(StylesDslContext.class)) {
                        RelationshipStyle relationshipStyle = new RelationshipStyleParser().parseRelationshipStyle(getContext(StylesDslContext.class), tokens.withoutContextStartToken());
                        startContext(new RelationshipStyleDslContext(relationshipStyle));

                    } else if (RELATIONSHIP_STYLE_THICKNESS_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseThickness(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if ((RELATIONSHIP_STYLE_COLOUR_TOKEN.equalsIgnoreCase(firstToken) || RELATIONSHIP_STYLE_COLOR_TOKEN.equalsIgnoreCase(firstToken)) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseColour(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_DASHED_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseDashed(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_OPACITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseOpacity(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_WIDTH_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseWidth(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_FONT_SIZE_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseFontSize(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_POSITION_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parsePosition(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_LINE_STYLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseLineStyle(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_ROUTING_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseRouting(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (RELATIONSHIP_STYLE_JUMP_TOKEN.equalsIgnoreCase(firstToken) && inContext(RelationshipStyleDslContext.class)) {
                        new RelationshipStyleParser().parseJump(getContext(RelationshipStyleDslContext.class), tokens);

                    } else if (DEPLOYMENT_ENVIRONMENT_TOKEN.equalsIgnoreCase(firstToken) && inContext(ModelDslContext.class)) {
                        String environment = new DeploymentEnvironmentParser().parse(tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentEnvironmentDslContext(environment));
                        }

                        registerIdentifier(identifier, new DeploymentEnvironment(environment));

                    } else if (DEPLOYMENT_GROUP_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentEnvironmentDslContext.class)) {
                        String group = new DeploymentGroupParser().parse(tokens.withoutContextStartToken());

                        registerIdentifier(identifier, new DeploymentGroup(getContext(DeploymentEnvironmentDslContext.class).getEnvironment(), group));

                    } else if (isElementKeywordOrArchetype(firstToken, DEPLOYMENT_NODE_TOKEN) && inContext(DeploymentEnvironmentDslContext.class)) {
                        Archetype archetype = getArchetype(DEPLOYMENT_NODE_TOKEN, firstToken);
                        DeploymentNode deploymentNode = new DeploymentNodeParser().parse(getContext(DeploymentEnvironmentDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentNodeDslContext(deploymentNode));
                        }

                        registerIdentifier(identifier, deploymentNode);
                    } else if (isElementKeywordOrArchetype(firstToken, DEPLOYMENT_NODE_TOKEN) && inContext(DeploymentNodeDslContext.class)) {
                        Archetype archetype = getArchetype(DEPLOYMENT_NODE_TOKEN, firstToken);
                        DeploymentNode deploymentNode = new DeploymentNodeParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new DeploymentNodeDslContext(deploymentNode));
                        }

                        registerIdentifier(identifier, deploymentNode);
                    } else if (isElementKeywordOrArchetype(firstToken, INFRASTRUCTURE_NODE_TOKEN) && inContext(DeploymentNodeDslContext.class)) {
                        Archetype archetype = getArchetype(INFRASTRUCTURE_NODE_TOKEN, firstToken);
                        InfrastructureNode infrastructureNode = new InfrastructureNodeParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken(), archetype);

                        if (shouldStartContext(tokens)) {
                            startContext(new InfrastructureNodeDslContext(infrastructureNode));
                        }

                        registerIdentifier(identifier, infrastructureNode);

                    } else if (INSTANCE_OF_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        StaticStructureElementInstance instance = new InstanceOfParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (instance instanceof SoftwareSystemInstance) {
                            if (shouldStartContext(tokens)) {
                                startContext(new SoftwareSystemInstanceDslContext((SoftwareSystemInstance)instance));
                            }
                        } else if (instance instanceof ContainerInstance) {
                            if (shouldStartContext(tokens)) {
                                startContext(new ContainerInstanceDslContext((ContainerInstance)instance));
                            }
                        }

                        registerIdentifier(identifier, instance);

                    } else if (SOFTWARE_SYSTEM_INSTANCE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        SoftwareSystemInstance softwareSystemInstance = new SoftwareSystemInstanceParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new SoftwareSystemInstanceDslContext(softwareSystemInstance));
                        }

                        registerIdentifier(identifier, softwareSystemInstance);

                    } else if (CONTAINER_INSTANCE_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentNodeDslContext.class)) {
                        ContainerInstance containerInstance = new ContainerInstanceParser().parse(getContext(DeploymentNodeDslContext.class), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new ContainerInstanceDslContext(containerInstance));
                        }

                        registerIdentifier(identifier, containerInstance);

                    } else if (HEALTH_CHECK_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticStructureElementInstanceDslContext.class)) {
                        new HealthCheckParser().parse(getContext(StaticStructureElementInstanceDslContext.class), tokens.withoutContextStartToken());
                    } else if (CUSTOM_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        CustomView view = new CustomViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new CustomViewDslContext(view));

                    } else if (SYSTEM_LANDSCAPE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        SystemLandscapeView view = new SystemLandscapeViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new SystemLandscapeViewDslContext(view));

                    } else if (SYSTEM_CONTEXT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        SystemContextView view = new SystemContextViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new SystemContextViewDslContext(view));

                    } else if (CONTAINER_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ContainerView view = new ContainerViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ContainerViewDslContext(view));

                    } else if (COMPONENT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ComponentView view = new ComponentViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ComponentViewDslContext(view));

                    } else if (DYNAMIC_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        DynamicView view = new DynamicViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new DynamicViewDslContext(view));

                    } else if (DEPLOYMENT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        DeploymentView view = new DeploymentViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new DeploymentViewDslContext(view));

                    } else if (FILTERED_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        FilteredView view = new FilteredViewParser().parse(getContext(), tokens.withoutContextStartToken());

                        if (shouldStartContext(tokens)) {
                            startContext(new FilteredViewDslContext(view));
                        }

                    } else if (IMAGE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        ImageView view = new ImageViewParser().parse(getContext(), tokens.withoutContextStartToken());
                        startContext(new ImageViewDslContext(view));

                    } else if (DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(firstToken) && inContext(DynamicViewDslContext.class)) {
                        startContext(new DynamicViewParallelSequenceDslContext(getContext(DynamicViewDslContext.class)));

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewContentParser().parseInclude(getContext(CustomViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewContentParser().parseExclude(getContext(CustomViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        new CustomViewAnimationStepParser().parse(getContext(CustomViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(CustomViewDslContext.class)) {
                        startContext(new CustomViewAnimationDslContext(getContext(CustomViewDslContext.class).getCustomView()));

                    } else if (inContext(CustomViewAnimationDslContext.class)) {
                        new CustomViewAnimationStepParser().parse(getContext(CustomViewAnimationDslContext.class), tokens);

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewContentParser().parseInclude(getContext(StaticViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewContentParser().parseExclude(getContext(StaticViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        new StaticViewAnimationStepParser().parse(getContext(StaticViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(StaticViewDslContext.class)) {
                        startContext(new StaticViewAnimationDslContext(getContext(StaticViewDslContext.class).getView()));

                    } else if (inContext(StaticViewAnimationDslContext.class)) {
                        new StaticViewAnimationStepParser().parse(getContext(StaticViewAnimationDslContext.class), tokens);

                    } else if (INCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewContentParser().parseInclude(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (EXCLUDE_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewContentParser().parseExclude(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (ANIMATION_STEP_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        new DeploymentViewAnimationStepParser().parse(getContext(DeploymentViewDslContext.class), tokens);

                    } else if (ANIMATION_IN_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(DeploymentViewDslContext.class)) {
                        startContext(new DeploymentViewAnimationDslContext(getContext(DeploymentViewDslContext.class).getView()));

                    } else if (inContext(DeploymentViewAnimationDslContext.class)) {
                        new DeploymentViewAnimationStepParser().parse(getContext(DeploymentViewAnimationDslContext.class), tokens);

                    } else if (AUTOLAYOUT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new AutoLayoutParser().parse(getContext(ModelViewDslContext.class), tokens);

                    } else if (DEFAULT_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new DefaultViewParser().parse(getContext(ViewDslContext.class));

                    } else if (VIEW_TITLE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new ViewParser().parseTitle(getContext(ViewDslContext.class), tokens);

                    } else if (VIEW_DESCRIPTION_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewDslContext.class)) {
                        new ViewParser().parseDescription(getContext(ViewDslContext.class), tokens);

                    } else if (PLANTUML_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser().parsePlantUML(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (MERMAID_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser().parseMermaid(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (KROKI_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser().parseKroki(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (IMAGE_VIEW_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class)) {
                        new ImageViewContentParser().parseImage(getContext(ImageViewDslContext.class), dslFile, tokens);

                    } else if (LIGHT_COLOR_SCHEME_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class) && shouldStartContext(tokens)) {
                        ImageViewDslContext context = getContext(ImageViewDslContext.class);
                        context.setColorScheme(ColorScheme.Light);
                        startContext(context);

                    } else if (DARK_COLOR_SCHEME_TOKEN.equalsIgnoreCase(firstToken) && inContext(ImageViewDslContext.class) && shouldStartContext(tokens)) {
                        ImageViewDslContext context = getContext(ImageViewDslContext.class);
                        context.setColorScheme(ColorScheme.Dark);
                        startContext(context);

                    } else if (inContext(DynamicViewDslContext.class)) {
                        RelationshipView relationshipView = new DynamicViewContentParser().parseRelationship(getContext(DynamicViewDslContext.class), tokens);

                        if (inContext(DynamicViewParallelSequenceDslContext.class)) {
                            getContext(DynamicViewParallelSequenceDslContext.class).hasRelationships(true);
                        }

                        if (shouldStartContext(tokens)) {
                            startContext(new DynamicViewRelationshipContext(relationshipView));
                        }

                    } else if (URL_TOKEN.equalsIgnoreCase(firstToken) && inContext(DynamicViewRelationshipContext.class)) {
                        new DynamicViewRelationshipParser().parseUrl(getContext(DynamicViewRelationshipContext.class), tokens.withoutContextStartToken());

                    } else if (THEME_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ViewsDslContext.class) || inContext(StylesDslContext.class))) {
                        new ThemeParser().parseTheme(getContext(), dslFile, tokens);

                    } else if (THEMES_TOKEN.equalsIgnoreCase(firstToken) && (inContext(ViewsDslContext.class) || inContext(StylesDslContext.class))) {
                        new ThemeParser().parseThemes(getContext(), dslFile, tokens);

                    } else if (TERMINOLOGY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ViewsDslContext.class)) {
                        startContext(new TerminologyDslContext());

                    } else if (PERSON_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parsePerson(getContext(), tokens);

                    } else if (SOFTWARE_SYSTEM_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseSoftwareSystem(getContext(), tokens);

                    } else if (CONTAINER_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseContainer(getContext(), tokens);

                    } else if (COMPONENT_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseComponent(getContext(), tokens);

                    } else if (DEPLOYMENT_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseDeploymentNode(getContext(), tokens);

                    } else if (INFRASTRUCTURE_NODE_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseInfrastructureNode(getContext(), tokens);

                    } else if (TERMINOLOGY_RELATIONSHIP_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseRelationship(getContext(), tokens);

                    } else if (METADATA_SYMBOLS_TOKEN.equalsIgnoreCase(firstToken) && inContext(TerminologyDslContext.class)) {
                        new TerminologyParser().parseMetadataSymbols(getContext(), tokens);

                    } else if (CONFIGURATION_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        startContext(new ConfigurationDslContext());

                    } else if (SCOPE_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        new ConfigurationParser().parseScope(getContext(), tokens);

                    } else if (VISIBILITY_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        new ConfigurationParser().parseVisibility(getContext(), tokens);

                    } else if (USERS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ConfigurationDslContext.class)) {
                        startContext(new UsersDslContext());

                    } else if (inContext(UsersDslContext.class)) {
                        new UserRoleParser().parse(getContext(), tokens);

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(WorkspaceDslContext.class)) {
                        if (features.isEnabled(Features.DOCUMENTATION)) {
                            new DocsParser().parse(getContext(WorkspaceDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DOCUMENTATION, firstToken + " is not permitted");
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(SoftwareSystemDslContext.class)) {
                        if (features.isEnabled(Features.DOCUMENTATION)) {
                            new DocsParser().parse(getContext(SoftwareSystemDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DOCUMENTATION, firstToken + " is not permitted");
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ContainerDslContext.class)) {
                        if (features.isEnabled(Features.DOCUMENTATION)) {
                            new DocsParser().parse(getContext(ContainerDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DOCUMENTATION, firstToken + " is not permitted");
                        }

                    } else if (DOCS_TOKEN.equalsIgnoreCase(firstToken) && inContext(ComponentDslContext.class)) {
                        if (features.isEnabled(Features.DOCUMENTATION)) {
                            new DocsParser().parse(getContext(ComponentDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DOCUMENTATION, firstToken + " is not permitted");
                        }

                    } else if ((ADRS_TOKEN.equalsIgnoreCase(firstToken) || DECISIONS_TOKEN.equalsIgnoreCase(firstToken)) && inContext(WorkspaceDslContext.class)) {
                        if (features.isEnabled(Features.DECISIONS)) {
                            new DecisionsParser().parse(getContext(WorkspaceDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DECISIONS, firstToken + " is not permitted");
                        }

                    } else if ((ADRS_TOKEN.equalsIgnoreCase(firstToken) || DECISIONS_TOKEN.equalsIgnoreCase(firstToken)) && inContext(SoftwareSystemDslContext.class)) {
                        if (features.isEnabled(Features.DECISIONS)) {
                            new DecisionsParser().parse(getContext(SoftwareSystemDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DECISIONS, firstToken + " is not permitted");
                        }

                    } else if ((ADRS_TOKEN.equalsIgnoreCase(firstToken) || DECISIONS_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ContainerDslContext.class)) {
                        if (features.isEnabled(Features.DECISIONS)) {
                            new DecisionsParser().parse(getContext(ContainerDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DECISIONS, firstToken + " is not permitted");
                        }

                    } else if ((ADRS_TOKEN.equalsIgnoreCase(firstToken) || DECISIONS_TOKEN.equalsIgnoreCase(firstToken)) && inContext(ComponentDslContext.class)) {
                        if (features.isEnabled(Features.DECISIONS)) {
                            new DecisionsParser().parse(getContext(ComponentDslContext.class), dslFile, tokens);
                        } else {
                            throw new FeatureNotEnabledException(Features.DECISIONS, firstToken + " is not permitted");
                        }

                    } else if (CONSTANT_TOKEN.equalsIgnoreCase(firstToken)) {
                        throw new RuntimeException("!constant was previously deprecated, and has now been removed - please use !const or !var instead");

                    } else if (CONST_TOKEN.equalsIgnoreCase(firstToken)) {
                        NameValuePair nameValuePair = new NameValueParser().parseConstant(tokens);
                        try {
                            addConstant(nameValuePair);
                        } catch (IllegalArgumentException e) {
                            throw new StructurizrDslParserException(e.getMessage());
                        }

                    } else if (VAR_TOKEN.equalsIgnoreCase(firstToken)) {
                        NameValuePair nameValuePair = new NameValueParser().parseVariable(tokens);
                        addVariable(nameValuePair);

                    } else if (IDENTIFIERS_TOKEN.equalsIgnoreCase(firstToken) && (inContext(WorkspaceDslContext.class) || inContext(ModelDslContext.class))) {
                        setIdentifierScope(new IdentifierScopeParser().parse(getContext(), tokens));

                    } else {
                        String[] expectedTokens;
                        if (getContext() == null) {
                            if (getWorkspace() == null) {
                                // the workspace hasn't yet been created
                                expectedTokens = new String[]{
                                        StructurizrDslTokens.WORKSPACE_TOKEN
                                };
                            } else {
                                expectedTokens = new String[0];
                            }
                        } else {
                            expectedTokens = getContext().getPermittedTokens();
                        }

                        if (expectedTokens.length > 0) {
                            StringBuilder buf = new StringBuilder();
                            for (String expectedToken : expectedTokens) {
                                buf.append(expectedToken);
                                buf.append(", ");
                            }
                            throw new StructurizrDslParserException("Unexpected tokens (expected: " + buf.substring(0, buf.length() - 2) + ")");
                        } else {
                            throw new StructurizrDslParserException("Unexpected tokens");
                        }
                    }
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    throw new StructurizrDslParserException(e.getMessage(), dslFile, dslLine.getLineNumber(), line);
                } else {
                    throw new StructurizrDslParserException(e.getClass().getSimpleName(), dslFile, dslLine.getLineNumber(), line);
                }
            }
        }

        if (!fragment && !contextStack.empty()) {
            throw new StructurizrDslParserException("Unexpected end of DSL content - are one or more closing curly braces missing?");
        }
    }

    private List<DslLine> preProcessLines(List<String> lines) {
        List<DslLine> dslLines = new ArrayList<>();

        int lineNumber = 1;
        StringBuilder buf = new StringBuilder();
        boolean lineComplete = true;
        boolean textBlock = false;
        int textBlockLeadingSpace = -1;

        for (String line : lines) {
            if (textBlock) {
                if (line.endsWith(TEXT_BLOCK_MARKER)) {
                    buf.append(TEXT_BLOCK_MARKER);
                    textBlock = false;
                    textBlockLeadingSpace = -1;
                    lineComplete = true;
                } else {
                    if (textBlockLeadingSpace == -1) {
                        textBlockLeadingSpace = 0;
                        for (int i = 0; i < line.length(); i++) {
                            if (Character.isWhitespace(line.charAt(i))) {
                                textBlockLeadingSpace++;
                            } else {
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNullOrEmpty(line)) {
                        buf.append("\n");
                    } else {
                        buf.append(line, textBlockLeadingSpace, line.length());
                        buf.append("\n");
                    }
                }
            } else if (!COMMENT_PATTERN.matcher(line).matches() && line.endsWith(MULTI_LINE_SEPARATOR)) {
                buf.append(line, 0, line.length() - 1);
                lineComplete = false;
            } else if (!COMMENT_PATTERN.matcher(line).matches() && line.endsWith(TEXT_BLOCK_MARKER)) {
                buf.append(line, 0, line.length());
                lineComplete = false;
                textBlock = true;
            } else {
                if (lineComplete) {
                    buf.append(line);
                } else {
                    buf.append(line.stripLeading());
                    lineComplete = true;
                }
            }

            if (lineComplete) {
                // replace the text block with a constant (that will become substituted later)
                // (this makes it possible for text blocks to include double-quote characters)
                String source = buf.toString();

                if (source.endsWith(TEXT_BLOCK_MARKER)) {
                    String[] parts = source.split(TEXT_BLOCK_MARKER);
                    String textBlockName = UUID.randomUUID().toString();
                    String textBlockValue = parts[1].substring(0, parts[1].length() - 1); // remove final line break
                    addTextBlock(textBlockName, textBlockValue);
                    dslLines.add(new DslLine(parts[0] + "\"" + String.format(STRING_SUBSTITUTION_TEMPLATE, textBlockName) + "\"", lineNumber));
                } else {
                    dslLines.add(new DslLine(source, lineNumber));
                }

                buf = new StringBuilder();
            }

            lineNumber++;
        }

        return dslLines;
    }

    private String substituteStrings(String token) {
        Matcher m = STRING_SUBSTITUTION_PATTERN.matcher(token);
        while (m.find()) {
            String before = m.group(0);
            String after = null;
            String name = before.substring(2, before.length()-1);
            if (constantsAndVariables.containsKey(name)) {
                NameValuePair nameValuePair = constantsAndVariables.get(name);

                if (nameValuePair.getType() == NameValueType.TextBlock) {
                    after = substituteStrings(nameValuePair.getValue());
                } else {
                    after = nameValuePair.getValue();
                }
            } else {
                if (getFeatures().isEnabled(Features.ENVIRONMENT)) {
                    String environmentVariable = System.getenv().get(name);
                    if (environmentVariable != null) {
                        after = environmentVariable;
                    }
                }
            }

            if (after != null) {
                token = token.replace(before, after);
            }
        }

        return token;
    }

    private boolean shouldStartContext(Tokens tokens) {
        return DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(tokens.get(tokens.size()-1));
    }

    private void startContext(DslContext context) {
        context.setWorkspace(workspace);
        context.setIdentifierRegister(identifiersRegister);
        context.setExtendingWorkspace(extendingWorkspace);
        context.setFeatures(features);
        context.setHttpClient(httpClient);
        contextStack.push(context);
    }

    private DslContext getContext() {
        if (!contextStack.empty()) {
            return contextStack.peek();
        } else {
            return null;
        }
    }

    private <T> T getContext(Class<T> clazz) throws StructurizrDslParserException {
        if (inContext(clazz)) {
            return (T)contextStack.peek();
        } else {
            throw new StructurizrDslParserException("Expected " + clazz.getName() + " but got " + contextStack.peek().getClass().getName());
        }
    }

    private void endContext() throws StructurizrDslParserException {
        if (!contextStack.empty()) {
            DslContext context = contextStack.pop();
            context.end();

            dslPortable &= context.isDslPortable();
        } else {
            throw new StructurizrDslParserException("Unexpected end of context");
        }
    }

    private boolean isGroup(DslContext context) {
        if (context instanceof GroupableDslContext) {
            return ((GroupableDslContext)context).hasGroup();
        }

        return false;
    }

    public Features getFeatures() {
        return features;
    }

    void setFeatures(Features features) {
        this.features = features;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private boolean isElementKeywordOrArchetype(String token, String keyword) {
        if (token.equalsIgnoreCase(keyword)) {
            return true;
        } else {
            return (archetypes.get(keyword).containsKey(token.toLowerCase()));
        }
    }

    private boolean isRelationshipKeywordOrArchetype(String token) {
        if (token.equalsIgnoreCase(RELATIONSHIP_TOKEN)) {
            return true;
        } else if (token.startsWith(RELATIONSHIP_ARCHETYPE_PREFIX) && token.endsWith(RELATIONSHIP_ARCHETYPE_SUFFIX)) {
            token = token.substring(RELATIONSHIP_ARCHETYPE_PREFIX.length(), token.length()-RELATIONSHIP_ARCHETYPE_SUFFIX.length());
            return (archetypes.get(RELATIONSHIP_TOKEN).containsKey(token.toLowerCase()));
        }

        return false;
    }

    private void addArchetype(Archetype archetype) {
        archetypes.get(archetype.getType()).put(archetype.getName(), archetype);
    }

    private Archetype getArchetype(String archetypeType, String archetypeName) {
        Archetype archetype = null;

        if (RELATIONSHIP_TOKEN.equals(archetypeType)) {
            if (archetypeName.startsWith(RELATIONSHIP_ARCHETYPE_PREFIX) && archetypeName.endsWith(RELATIONSHIP_ARCHETYPE_SUFFIX)) {
                archetypeName = archetypeName.substring(RELATIONSHIP_ARCHETYPE_PREFIX.length(), archetypeName.length() - RELATIONSHIP_ARCHETYPE_SUFFIX.length());
            }
        }
        archetype = archetypes.get(archetypeType).get(archetypeName.toLowerCase());

        if (archetype == null) {
            archetype = new Archetype(archetypeName, archetypeType);
        }

        return archetype;
    }

    private void extendArchetype(Archetype archetype, String archetypeName) {
        archetypeName = archetypeName.toLowerCase();
        Archetype parentArchetype = getArchetype(archetype.getType(), archetypeName);

        archetype.setMetadata(parentArchetype.getMetadata());
        archetype.setDescription(parentArchetype.getDescription());
        archetype.setTechnology(parentArchetype.getTechnology());
        archetype.addTags(parentArchetype.getTags().toArray(new String[0]));
    }

    /**
     * Gets the identifier register in use (this is the mapping of DSL identifiers to elements/relationships).
     *
     * @return      an IdentifiersRegister object
     */
    public IdentifiersRegister getIdentifiersRegister() {
        return identifiersRegister;
    }

    void registerIdentifier(String identifier, Element element) {
        identifiersRegister.register(identifier, element);
        element.addProperty(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME, identifiersRegister.findIdentifier(element));
    }

    void registerIdentifier(String identifier, Relationship relationship) {
        identifiersRegister.register(identifier, relationship);

        if (!StringUtils.isNullOrEmpty(identifier)) {
            relationship.addProperty(STRUCTURIZR_DSL_IDENTIFIER_PROPERTY_NAME, identifiersRegister.findIdentifier(relationship));
        }
    }

    /**
     * Gets the named constant.
     *
     * @param name      the name of the constant
     * @return  the value, or an empty string if the named constant doesn't exist
     */
    public String getConstant(String name) {
        NameValuePair nameValuePair = constantsAndVariables.get(name);
        if (nameValuePair != null) {
            return nameValuePair.getValue();
        } else {
            return "";
        }
    }

    /**
     * Adds a constant to the parser.
     * @param name      the name of the constant
     * @param value     the value of the constant
     */
    public void addConstant(String name, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A constant name must be specified");
        }

        addConstant(new NameValuePair(name, value));
    }

    private void addConstant(NameValuePair nameValuePair) {
        if (constantsAndVariables.containsKey(nameValuePair.getName())) {
            throw new IllegalArgumentException("A constant/variable \"" + nameValuePair.getName() + "\" already exists");
        }
        constantsAndVariables.put(nameValuePair.getName(), nameValuePair);
    }

    private void addVariable(NameValuePair nameValuePair) {
        if (constantsAndVariables.containsKey(nameValuePair.getName()) && constantsAndVariables.get(nameValuePair.getName()).getType() == NameValueType.Constant) {
            throw new IllegalArgumentException("A constant \"" + nameValuePair.getName() + "\" already exists");
        }
        constantsAndVariables.put(nameValuePair.getName(), nameValuePair);
    }

    private void addTextBlock(String name, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A text block name must be specified");
        }

        NameValuePair nameValuePair = new NameValuePair(name, value);
        nameValuePair.setType(NameValueType.TextBlock);
        addConstant(nameValuePair);
    }

    private boolean inContext(Class clazz) {
        if (contextStack.empty()) {
            return false;
        }

        return clazz.isAssignableFrom(contextStack.peek().getClass());
    }

}