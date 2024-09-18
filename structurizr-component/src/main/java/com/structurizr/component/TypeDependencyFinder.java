package com.structurizr.component;

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class TypeDependencyFinder {

    private static final Log log = LogFactory.getLog(TypeDependencyFinder.class);

    void run(Type type, TypeRepository typeRepository) {
        log.debug("Type dependencies for " + type.getFullyQualifiedName() + ":");
        ConstantPool cp = type.getJavaClass().getConstantPool();
        ConstantPoolGen cpg = new ConstantPoolGen(cp);
        for (Method m : type.getJavaClass().getMethods()) {
            MethodGen mg = new MethodGen(m, type.getJavaClass().getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            if (il == null) {
                continue;
            }

            InstructionHandle[] instructionHandles = il.getInstructionHandles();
            for (InstructionHandle instructionHandle : instructionHandles) {
                Instruction instruction = instructionHandle.getInstruction();
                if (!(instruction instanceof InvokeInstruction)) {
                    continue;
                }

                InvokeInstruction invokeInstruction = (InvokeInstruction)instruction;
                ReferenceType referenceType = invokeInstruction.getReferenceType(cpg);
                if (!(referenceType instanceof ObjectType)) {
                    continue;
                }

                ObjectType objectType = (ObjectType)referenceType;
                String referencedClassName = objectType.getClassName();
                com.structurizr.component.Type referencedType = typeRepository.getType(referencedClassName);
                if (referencedType != null && !referencedType.getFullyQualifiedName().equals(type.getFullyQualifiedName()) && !type.hasDependency(referencedType)) {
                    log.debug(" + " + referencedType.getFullyQualifiedName());

                    type.addDependency(referencedType);
                }
            }
        }

        if (type.getDependencies().isEmpty()) {
            log.debug(" - none");
        }
    }

}
