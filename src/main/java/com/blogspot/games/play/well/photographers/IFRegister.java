package com.blogspot.games.play.well.photographers;

import java.util.List;

/**
 * User: patronus
 */
public interface IFRegister {
    int getPage();

    void setPage(int page);

    List<Image> getImages();

    void addImages(List<Image> images);

    int getImageProcessed();

    void setImageProcessed(int to);
}
