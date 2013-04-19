package com.example.photographers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.photographers.adapter.EndlessScrollListener;
import com.example.photographers.adapter.MainPageListAdapter;
import com.example.photographers.services.LazyLoader;
import com.example.photographers.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class AcPre extends Activity {
    public static final String IMAGES = "images";
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


        ListView list = (ListView) findViewById(R.id.endless_list);
        ArrayList<Image> images = new ArrayList<Image>();
        if (savedInstanceState != null) {
            Object o = savedInstanceState.get(IMAGES);
            if (o != null) {
                images = (ArrayList<Image>) o;
            }
        }
        adapter = new MainPageListAdapter(this, images);

        //Set global register
        ImageRegister.getInstance().setImages(adapter.getImages());
        list.setAdapter(adapter);
        //Listeners etc
        list.setOnScrollListener(new EndlessScrollListener(5, this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AcPre.this, AcBig.class);
                intent.putExtra(AcBig.POSITION, position);
                startActivity(intent);
            }
        });

        registerBroadcastReceiver();
        //First start
        Intent service = new Intent(this, LazyLoader.class);
        service.putExtra(LazyLoader.PAGE, 0);
        startService(service);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(IMAGES, adapter.getImages());

        super.onSaveInstanceState(outState);
    }

    private void registerBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("Received [" + action + "]");
                if (LazyLoader.NEW_ELEMENT.equals(action)) {
                    Image img = (Image) intent.getExtras().get(LazyLoader.ITEM);
                    Log.d("new item:" + img);
                    adapter.addNew(img);
                    adapter.notifyDataSetChanged();
                } else if (LazyLoader.PAGE_LOADED.equals(action)) {
                    adapter.notifyDataSetChanged();
                }


            }
        };
        registerReceiver(receiver, new IntentFilter() {{
//            addAction(LazyLoader.PAGE_LOADED);
            addAction(LazyLoader.NEW_ELEMENT);
        }}
        );
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
