package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructurizrClientTests {

    @Test
    public void test_setUrl_RemovesTheTrailingSlash_WhenATrailingSlashIsAdded() {
        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com/", "key", "secret");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());

        structurizrClient.setUrl("https://api.structurizr.com/");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());
    }

    @Test
    public void test_setUrl() {
        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());

        structurizrClient.setUrl("https://api.structurizr.com");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());
    }

}
