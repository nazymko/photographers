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
import com.actionbarsherlock.widget.ShareActionProvider;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.adapter.BigScreenAdapter;
import com.blogspot.games.play.well.photographers.services.FileLoader;
import com.blogspot.games.play.well.photographers.services.LazyLoader;
import com.blogspot.games.play.well.photographers.util.Dif;
import com.blogspot.games.play.well.photographers.util.Log;

import java.util.List;

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
        actionBar.setIcon(R.drawable.icon_v2_small);
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
        Image image;
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.menu_save:
                image = ImageNormalRegister.getInstance().getImages().get(picturePosition);
                //Okay, i load it from the internet
                Log.d("Load service before start");
                intent = new Intent(this, FileLoader.class);
                intent.putExtra(FileLoader.FILE_URL, image.getBigImage());
                startService(intent);

                break;
            case R.id.menu_share:
                //Share image url on the site

                image = ImageNormalRegister.getInstance().getImages().get(picturePosition);
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
                if (FileLoader.FILE_LOADED.equals(action)) {
                    Bundle extras = intent.getExtras();
                    //We load some image
                    Toast.makeText(AcBig.this, "Image saved :)", Toast.LENGTH_LONG).show();

                } else if (LazyLoader.PAGE_LOADED.equals(action)) {
                    adapter.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter() {{
            addAction(FileLoader.FILE_LOADED);
            addAction(LazyLoader.PAGE_LOADED);
        }}
        );
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }
}