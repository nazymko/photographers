package com.example.photographers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.photographers.adapter.PagerAdapter;
import com.example.photographers.services.LazyLoader;

/**
 * User: Andrew.Nazymko
 */
public class AcBigPicture extends FragmentActivity {

    public static final String POSITION = "POSITION";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        registerReceiver();
        int position = getIntent().getIntExtra(POSITION, 0);
        setContentView(R.layout.big_picture);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //Create adapter for images
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), Cache.getInstance().getCache());

        pager.setAdapter(adapter);

        pager.setCurrentItem(position);

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