package com.blogspot.games.play.well.photographers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: patronus
 */
public class ImageAuthorRegister implements IFRegister {
    private static IFRegister ourInstance = new ImageAuthorRegister();
    private List<Image> images = new ArrayList<Image>();
    private int page = 0;
    private int done;


    private ImageAuthorRegister() {
    }

    public static IFRegister getInstance() {
        return ourInstance;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public List<Image> getImages() {
        return images;
    }

    @Override
    public void addImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public int getImageProcessed() {
        return done;
    }

    @Override
    public void setImageProcessed(int to) {
        this.done = to;
    }


}
