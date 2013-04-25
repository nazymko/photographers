package com.blogspot.games.play.well.photographers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: patronus
 */
public class ImageAuthorCache {
    private static ImageAuthorCache ourInstance = new ImageAuthorCache();
    List<Image> images = new ArrayList<Image>();

    private ImageAuthorCache() {
    }

    public static ImageAuthorCache getInstance() {
        return ourInstance;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


}
