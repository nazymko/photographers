package com.blogspot.games.play.well.photographers.ac;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;


import com.actionbarsherlock.view.Menu;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.adapter.EndlessScrollListener;
import com.blogspot.games.play.well.photographers.adapter.MainPageListAdapter;
import com.blogspot.games.play.well.photographers.services.FeedAuthorLoader;
import com.blogspot.games.play.well.photographers.services.FeedNormalLoader;
import com.blogspot.games.play.well.photographers.services.FileSaver;
import com.blogspot.games.play.well.photographers.util.Dif;
import com.blogspot.games.play.well.photographers.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class AcPre extends SherlockActivity {
    public static final String IMAGES = "img";
    public static final int MORE_FROM_AUTHOR = 1;
    public static final int SHARE_LINK = 2;
    public static final int SAVE_FILE = 3;
    public static String BASE_PATH = "http://photographers.com.ua";
    private MainPageListAdapter adapter;
    private BroadcastReceiver receiver;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_list);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .build();
        ImageLoader.getInstance().init(config);

        //Add action bar
        ActionBar actionBar = getActionBar();
//        actionBar.setIcon(R.drawable.icon_v2_small);


        //Set previously loaded list element
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
        ImageNormalRegister.getInstance().addImages(adapter.getImages());
        list.setAdapter(adapter);
        //Listeners etc
        list.setOnScrollListener(new EndlessScrollListener(this, FeedNormalLoader.class));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AcPre.this, AcBig.class);
                intent.putExtra(AcBig.POSITION, position);
                intent.putExtra(AcBig.MODE, AcBig.MODE_NORMAL);
                String author = ImageNormalRegister.getInstance().getImages().get(position).getAuthor();
                intent.putExtra(AcAuthor.NAME, author);
                startActivity(intent);
            }
        });

        registerForContextMenu(list);

        registerBroadcastReceiver();
        //First start
        Intent service = new Intent(this, FeedNormalLoader.class);
        service.putExtra(FeedNormalLoader.PAGE, 0);
        startService(service);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(IMAGES, adapter.getImages());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(MORE_FROM_AUTHOR, 0, 0, "More from this photographer");
        menu.add(SHARE_LINK, 0, 0, "Share link with");
        menu.add(SAVE_FILE, 0, 0, "Save image");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSherlock().getMenuInflater().inflate(R.menu.feed_back, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int groupId = item.getGroupId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Image image = ImageNormalRegister.getInstance().getImages().get(info.position);
        //get selected element position in adapter
        Log.d("Position:" + info.position);

        Log.w("Item:" + image.toString());
        switch (groupId) {
            case MORE_FROM_AUTHOR:
                Intent acStart = new Intent(this, AcAuthor.class);
                acStart.putExtra(FeedAuthorLoader.PAGE_URL, image.getAuthorPage());
                acStart.putExtra(FeedAuthorLoader.AUTHOR_NAME, image.getAuthor());
                startActivity(acStart);
                break;
            case SHARE_LINK:
                Dif.share(this, image);
                break;
            case SAVE_FILE:
                //Okay, i load it from the internet
                Log.d("Load service before start");
                Intent intent = new Intent(this, FileSaver.class);
                intent.putExtra(FileSaver.FILE_URL, image.getBigImageUrl());

                startService(intent);

                break;
        }

        return super.onContextItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void registerBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("Received [" + action + "]");
                if (FeedNormalLoader.NEW_ELEMENT.equals(action)) {
                    Image img = (Image) intent.getExtras().get(FeedNormalLoader.ITEM);
                    Log.d("new item:" + img);
                    adapter.addNew(img);
                    adapter.notifyDataSetChanged();
                } else if (FeedNormalLoader.ACTION_PAGE_LOADED.equals(action)) {
                    adapter.addImages(ImageNormalRegister.getInstance().getImages());
                    adapter.notifyDataSetChanged();
                } else if (FileSaver.ACTION_FILE_LOADED.equals(action)) {
                    String file = intent.getStringExtra(FileSaver.FILE);
                    Toast.makeText(AcPre.this, "Image saved :)\n" + file, Toast.LENGTH_SHORT).show();
                } else if (FileSaver.ACTION_FILE_NOT_LOADED.equals(action)) {

                    Toast.makeText(AcPre.this, "Sorry, adult content :(", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter() {{
            addAction(FeedNormalLoader.ACTION_PAGE_LOADED);
            addAction(FileSaver.ACTION_FILE_LOADED);
            addAction(FileSaver.ACTION_FILE_NOT_LOADED);
        }}
        );
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.menu_feed_back) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " feed back");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email)});

            Log.d("Send feedback action");
            sendBroadcast(intent);
            startActivity(Intent.createChooser(intent, "Feedback via..."));


        }
        return super.onMenuItemSelected(featureId, item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        ImageAuthorRegister.getInstance().getImages().clear();//Clean author cache
        super.onStart();
    }
}
