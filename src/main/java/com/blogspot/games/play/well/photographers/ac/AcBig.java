package com.blogspot.games.play.well.photographers.ac;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.adapter.BigScreenAdapter;
import com.blogspot.games.play.well.photographers.services.FeedNormalLoader;
import com.blogspot.games.play.well.photographers.services.FileSaver;
import com.blogspot.games.play.well.photographers.util.Dif;
import com.blogspot.games.play.well.photographers.util.Log;

/**
 * User: Andrew.Nazymko
 */
public class AcBig extends SherlockFragmentActivity {

    public static final String POSITION = "POSITION";
    public static final String MODE = "MODE";
    public static final int MODE_NORMAL = 1;
    public static final int MODE_AUTHOR = 2;
    private ViewPager pager;
    private int picturePosition = 0;
    private BigScreenAdapter adapter;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picturePosition = getIntent().getIntExtra(POSITION, 0);
        int mode = getIntent().getIntExtra(MODE, 0);

        setContentView(R.layout.ac_scale);
        Log.w("Position:" + picturePosition);

        pager = (ViewPager) findViewById(R.id.pager);


        adapter = new BigScreenAdapter(this, getSupportFragmentManager(), mode);
        pager.setAdapter(adapter);

        pager.setCurrentItem(picturePosition);



        //add save action bar item

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        String stringExtra = getIntent().getStringExtra(AcAuthor.NAME);
        if (stringExtra != null) {
            actionBar.setTitle(stringExtra);
        }
//        actionBar.setIcon(R.drawable.icon_v2_small);
        registerReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.big_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Image image = adapter.getImages().get(picturePosition);
        Intent intent;
        int currentItem = pager.getCurrentItem();
        Log.d("AcBig : selected item :" + currentItem);
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.menu_save:
                //Okay, i load it from the internet
                Log.d("Load service before start");
                intent = new Intent(this, FileSaver.class);
                intent.putExtra(FileSaver.FILE_URL, image.getBigImageUrl());
                startService(intent);

                break;
            case R.id.menu_share:
                //Share image url on the site
                Dif.share(this, image);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("action = [" + action + "]");
                if (FileSaver.ACTION_FILE_LOADED.equals(action)) {
                    String file = intent.getStringExtra(FileSaver.FILE);
                    Toast.makeText(AcBig.this, "Image saved :)\n" + file, Toast.LENGTH_SHORT).show();
                } else if (FileSaver.ACTION_FILE_NOT_LOADED.equals(action)) {
                    Toast.makeText(AcBig.this, "Sorry, adult content :(", Toast.LENGTH_SHORT).show();
                } else if (FeedNormalLoader.ACTION_PAGE_LOADED.equals(action)) {
                    adapter.notifyDataSetChanged();
                } else if (FeedNormalLoader.ACTION_NAME_CHANGE.equals(action)) {
                    String stringExtra = intent.getStringExtra(AcAuthor.NAME);
                    if (stringExtra != null && stringExtra.trim().length() != 0) {
                        getActionBar().setTitle(stringExtra);
                    } else {
                        getActionBar().setTitle(getString(R.string.unnamed_image));

                    }
                }
            }
        };
        registerReceiver(receiver, new IntentFilter() {{
            addAction(FileSaver.ACTION_FILE_LOADED);
            addAction(FileSaver.ACTION_FILE_NOT_LOADED);
            addAction(FeedNormalLoader.ACTION_PAGE_LOADED);
            addAction(FeedNormalLoader.ACTION_NAME_CHANGE);
        }}
        );
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}