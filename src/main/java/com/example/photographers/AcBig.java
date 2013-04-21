package com.example.photographers;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.photographers.adapter.BigScreenAdapter;
import com.example.photographers.dragimg.Frame;
import com.example.photographers.services.FileLoader;
import com.example.photographers.services.LazyLoader;
import com.example.photographers.util.Log;

import java.io.File;

/**
 * User: Andrew.Nazymko
 */
public class AcBig extends SherlockFragmentActivity {

    public static final String POSITION = "POSITION";
    private ShareActionProvider mShareActionProvider;
    private ViewPager pager;
    private int picturePosition = 0;
    private BigScreenAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picturePosition = getIntent().getIntExtra(POSITION, 0);
        setContentView(R.layout.ac_scale);
        Log.w("Position:" + picturePosition);

        pager = (ViewPager) findViewById(R.id.pager);


        adapter = new BigScreenAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        pager.setCurrentItem(picturePosition);


        //add save action bar item

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        registerReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.big_item_menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;

            case R.id.menu_save:
                Image image = ImageRegister.getInstance().getImages().get(picturePosition);

                //Okay, i load it from the internet
                Log.d("Load service before start");
                Intent intent = new Intent(this, FileLoader.class);
                intent.putExtra(FileLoader.FILE_URL, image.getBigImage());
                startService(intent);


                break;
            case R.id.menu_share:
                Intent shareIntent = new Intent();
                shareIntent.setType("*/*");
//                shareIntent.
                int currentItem = pager.getCurrentItem();
                String bigImage = ImageRegister.getInstance().getImages().get(currentItem).getBigImage();

                Uri uri = Uri.fromFile(new File(getFilesDir(), bigImage));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());

                setShareIntent(shareIntent);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void registerReceiver() {
        registerReceiver(new BroadcastReceiver() {
                             @Override
                             public void onReceive(Context context, Intent intent) {
                                 String action = intent.getAction();
                                 Log.d("action = [" + action + "]");
                                 if (FileLoader.FILE_LOADED.equals(action)) {

                                     Toast.makeText(AcBig.this, "Image saved :)", Toast.LENGTH_LONG).show();
                                 } else if (LazyLoader.PAGE_LOADED.equals(action)) {
                                     adapter.notifyDataSetChanged();
                                 }
                             }
                         }, new IntentFilter() {{
                             addAction(FileLoader.FILE_LOADED);
                             addAction(LazyLoader.PAGE_LOADED);
                         }}
        );
    }

}