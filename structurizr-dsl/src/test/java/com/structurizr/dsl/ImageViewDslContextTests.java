package com.structurizr.dsl;

import com.structurizr.view.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImageViewDslContextTests extends AbstractTests {

    @Test
    void end_ThrowsAnException_WhenThereIsNoContent() {
        try {
            ImageView imageView = workspace.getViews().createImageView("key");
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.end();
            fail();
        } catch (Exception e) {
            assertEquals("The image view \"key\" has no content", e.getMessage());
        }
    }

    @Test
    void end_WhenThereIsContent() {
        ImageView imageView = workspace.getViews().createImageView("key");
        imageView.setContent("http://example.com/image.png");
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.end();
    }

}