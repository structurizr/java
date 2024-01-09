package com.structurizr.api;

public class WorkspaceMetadata {

    private int id;
    private String name;
    private String description;
    private String apiKey;
    private String apiSecret;

    private String privateUrl;
    private String publicUrl;
    private String shareableUrl;

    WorkspaceMetadata() {
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getApiKey() {
        return apiKey;
    }

    void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getPrivateUrl() {
        return privateUrl;
    }

    void setPrivateUrl(String privateUrl) {
        this.privateUrl = privateUrl;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getShareableUrl() {
        return shareableUrl;
    }

    void setShareableUrl(String shareableUrl) {
        this.shareableUrl = shareableUrl;
    }

}