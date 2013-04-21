package com.example.photographers;

import java.util.List;

/**
 * User: patronus
 */
public class ImageRegister {
    private static ImageRegister ourInstance = new ImageRegister();
    List<Image> images;
    int imageProcessed = 0;
    private int currentPage;

    private ImageRegister() {
    }

    public static ImageRegister getInstance() {
        return ourInstance;
    }

    public int getImageProcessed() {
        return imageProcessed;
    }

    public void setImageProcessed(int imageProcessed) {
        this.imageProcessed = imageProcessed;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
