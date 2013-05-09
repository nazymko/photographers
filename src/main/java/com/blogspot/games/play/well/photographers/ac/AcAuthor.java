package com.blogspot.games.play.well.photographers.ac;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.adapter.AuthorAdapter;
import com.blogspot.games.play.well.photographers.adapter.EndlessScrollListener;
import com.blogspot.games.play.well.photographers.services.FeedAuthorLoader;
import com.blogspot.games.play.well.photographers.services.FileSaver;
import com.blogspot.games.play.well.photographers.util.Dif;
import com.blogspot.games.play.well.photographers.util.Log;

/**
 * User: patronus
 */
public class AcAuthor extends SherlockActivity {
    public static final IFRegister REGISTER = ImageAuthorRegister.getInstance();
    private static final int SHARE_LINK = 1;
    private static final int SAVE_FILE = 2;
    public static String NAME = "NAME";
    private AuthorAdapter adapter;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_list);


        //Load first page
        String pageUrl = getIntent().getStringExtra(FeedAuthorLoader.PAGE_URL);
        Intent intent = new Intent(this, FeedAuthorLoader.class);
        intent.putExtra(FeedAuthorLoader.PAGE_URL, pageUrl);
        startService(intent);



        //TODO:Make this fine
        adapter = new AuthorAdapter(this, ImageAuthorRegister.getInstance().getImages());

        ListView list = (ListView) findViewById(R.id.endless_list);
        list.setAdapter(adapter);

        //Create and initialize listener  - add author page for example
        EndlessScrollListener l = new EndlessScrollListener(this, FeedAuthorLoader.class);
        registerForContextMenu(list);
        l.putParam(FeedAuthorLoader.PAGE_URL, pageUrl);
        list.setOnScrollListener(l);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AcAuthor.this, AcBig.class);
                intent.putExtra(AcBig.MODE, AcBig.MODE_AUTHOR);
                intent.putExtra(AcBig.POSITION, position);
                startActivity(intent);
            }
        });


        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setIcon(R.drawable.icon_v2_small);

        //Get author from intent
        String name = getIntent().getExtras().getString(NAME);

        //Change title
        actionBar.setTitle(name);

        registerReceiver();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(FeedAuthorLoader.AUTHOR_PAGE_LOADED));
        super.onResume();
    }

    private void registerReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (FeedAuthorLoader.AUTHOR_PAGE_LOADED.equals(action)) {
                    adapter.addAll(ImageAuthorRegister.getInstance().getImages());
                    adapter.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(FeedAuthorLoader.AUTHOR_PAGE_LOADED));

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(SHARE_LINK, 0, 0, "Share link with");
        menu.add(SAVE_FILE, 0, 0, "Save image");
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        int groupId = item.getGroupId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Image image = REGISTER.getImages().get(info.position);
        //get selected element position in adapter
        Log.d("Position:" + info.position);

        Log.w("Item:" + image.toString());
        switch (groupId) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                break;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
