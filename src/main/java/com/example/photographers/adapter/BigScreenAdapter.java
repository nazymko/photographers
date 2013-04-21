package com.example.photographers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.photographers.ImageRegister;
import com.example.photographers.dragimg.Frame;
import com.example.photographers.services.LazyLoader;

/**
 * User: patronus
 */
public class BigScreenAdapter extends FragmentPagerAdapter {
    public static final int WRAP_COUNT = 5;
    private final Context context;
    boolean loading = false;

    public BigScreenAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i < getCount() - WRAP_COUNT) {
            loading = true;
            Intent intent = new Intent(context, LazyLoader.class);
            intent.putExtra(LazyLoader.PAGE, ImageRegister.getInstance().getCurrentPage() + 1);
            context.startService(intent);
        } else {
            loading = false;
        }

        return new Frame(i);
    }

    @Override
    public int getCount() {
        return ImageRegister.getInstance().getImages().size();
    }
}
