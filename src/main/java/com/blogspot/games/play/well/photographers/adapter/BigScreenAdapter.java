package com.blogspot.games.play.well.photographers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.ac.AcBig;
import com.blogspot.games.play.well.photographers.dragimg.ImageFrame;
import com.blogspot.games.play.well.photographers.services.FeedAuthorLoader;
import com.blogspot.games.play.well.photographers.services.FeedNormalLoader;

import java.util.List;

/**
 * User: patronus
 */
public class BigScreenAdapter extends FragmentPagerAdapter {
    public static final int WRAP_COUNT = 5;
    private final Context context;
    private final List<Image> images;
    boolean loading = false;
    private int mode;

    public List<Image> getImages() {
        return images;
    }

    public BigScreenAdapter(Context context, FragmentManager fm, int mode) {
        super(fm);
        this.context = context;
        this.mode = mode;


        this.images = getSource(mode).getImages();

    }

    @Override
    public Fragment getItem(int i) {
        if (i < getCount() - WRAP_COUNT) {
            loading = true;
            Intent intent = new Intent(context, getLoader(mode));
            intent.putExtra(FeedNormalLoader.PAGE, getSource(mode).getPage() + 1);

            context.startService(intent);
        } else {
            loading = false;

        }
        return new ImageFrame(i, getSource(mode));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    IFRegister getSource(int mode) {
        switch (mode) {
            case AcBig.MODE_NORMAL:
                return ImageNormalRegister.getInstance();

            case AcBig.MODE_AUTHOR:
                return ImageAuthorRegister.getInstance();

        }
        return null;
    }

    private Class getLoader(int mode) {
        switch (mode) {
            case AcBig.MODE_AUTHOR:
                return FeedAuthorLoader.class;
            case AcBig.MODE_NORMAL:
                return FeedNormalLoader.class;
        }
        return null;
    }


}
