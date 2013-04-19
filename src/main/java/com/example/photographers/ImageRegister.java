package com.example.photographers;

import java.util.List;

/**
 * User: patronus
 */
public class ImageRegister {
    private static ImageRegister ourInstance = new ImageRegister();
    List<Image> images;

    private ImageRegister() {
    }

    public static ImageRegister getInstance() {
        return ourInstance;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
