package com.structurizr.component.description;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FirstSentenceDescriptionStrategyTests {

    @Test
    void descriptionOf_WhenTheDescriptionIsNull() {
        Type type = new Type("com.example.ClassName");
        type.setDescription(null);
        assertEquals("", new FirstSentenceDescriptionStrategy().descriptionOf(type));
    }

    @Test
    void descriptionOf_WhenTheDescriptionIsEmpty() {
        Type type = new Type("com.example.ClassName");
        type.setDescription(" ");
        assertEquals("", new FirstSentenceDescriptionStrategy().descriptionOf(type));
    }

    @Test
    void descriptionOf_WhenThereIsASentence() {
        Type type = new Type("com.example.ClassName");
        type.setDescription("This is the first sentence. And this is the second.");
        assertEquals("This is the first sentence.", new FirstSentenceDescriptionStrategy().descriptionOf(type));
    }

    @Test
    void descriptionOf_WhenThereIsNotASentence() {
        Type type = new Type("com.example.ClassName");
        type.setDescription("This is just lots of text without any punctuation");
        assertEquals("This is just lots of text without any punctuation", new FirstSentenceDescriptionStrategy().descriptionOf(type));
    }

}