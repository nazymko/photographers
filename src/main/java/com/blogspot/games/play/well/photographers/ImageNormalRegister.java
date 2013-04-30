package com.blogspot.games.play.well.photographers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: patronus
 */
public class ImageNormalRegister implements IFRegister {
    private static ImageNormalRegister ourInstance = new ImageNormalRegister();
    List<Image> images = new ArrayList<Image>();
    int imageProcessed = 0;
    private int page;

    private ImageNormalRegister() {
    }

    public static ImageNormalRegister getInstance() {
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

    public void addImages(List<Image> images) {
        for (Image image : images) {
            if (!images.contains(image)) {
                images.add(image);
            }
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
