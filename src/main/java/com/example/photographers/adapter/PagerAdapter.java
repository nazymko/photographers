package com.example.photographers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.photographers.Image;

import java.util.List;

/**
 * User: Andrew.Nazymko
 */
public class PagerAdapter extends FragmentPagerAdapter {
    int loadLimit = 2;

    public PagerAdapter(FragmentManager fm, List<Image> elements) {
        super(fm);

    }



    @Override
    public Fragment getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;  //TODO:It is correct method body?
    }
}
