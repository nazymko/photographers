package com.example.photographers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.photographers.dragimg.Frame;
import com.example.photographers.services.LazyLoader;

/**
 * User: Andrew.Nazymko
 */
public class AcBig extends FragmentActivity {

    public static final String POSITION = "POSITION";

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getIntent().getIntExtra("position", 0);
        setContentView(R.layout.ac_scale);


        ViewPager pager = (ViewPager) findViewById(R.id.pager);


        pager.setCurrentItem(position);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return new Frame(i);
            }

            @Override
            public int getCount() {
                return ImageRegister.getInstance().getImages().size();
            }
        });

    }

    private void registerReceiver() {
        registerReceiver(new BroadcastReceiver() {
                             @Override
                             public void onReceive(Context context, Intent intent) {

                             }
                         }, new IntentFilter() {{
                             addAction(LazyLoader.PAGE_LOADED);
                         }}
        );
    }
}