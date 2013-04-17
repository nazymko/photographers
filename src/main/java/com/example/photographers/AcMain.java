package com.example.photographers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import com.example.photographers.adapter.EndlessScrollListener;
import com.example.photographers.adapter.MainPageListAdapter;
import com.example.photographers.services.LazyLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AcMain extends Activity {
    private MainPageListAdapter adapter;
    private BroadcastReceiver receiver;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .build();
        ImageLoader.getInstance().init(config);


        ListView list = (ListView) findViewById(R.id.endles_list);

        adapter = new MainPageListAdapter(this, Cache.getInstance().getCache());

        list.setAdapter(adapter);
        //Listeners etc
        list.setOnScrollListener(new EndlessScrollListener(5, this));


        registerBroadcastReceiver();
        //First start
        Intent service = new Intent(this, LazyLoader.class);
        service.putExtra(LazyLoader.PAGE, 0);
        startService(service);
    }

    private void registerBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Received [" + intent.getAction() + "]");
                adapter.setImages(Cache.getInstance().getCache());
                adapter.notifyDataSetChanged();
            }
        };
        registerReceiver(receiver, new IntentFilter() {{
            addAction(LazyLoader.PAGE_LOADED);
            addAction(LazyLoader.FEW_ELEMENTS_ADDED);
        }}
        );
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
