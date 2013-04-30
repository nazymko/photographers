package com.blogspot.games.play.well.photographers.ac;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.adapter.AuthorAdapter;
import com.blogspot.games.play.well.photographers.adapter.EndlessScrollListener;
import com.blogspot.games.play.well.photographers.services.AuthorLoader;

/**
 * User: patronus
 */
public class AcAuthor extends SherlockActivity {
    public static String NAME = "NAME";
    private AuthorAdapter adapter;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_list);


        //Load first page
        String pageUrl = getIntent().getStringExtra(AuthorLoader.PAGE_URL);
        Intent intent = new Intent(this, AuthorLoader.class);
        intent.putExtra(AuthorLoader.PAGE_URL, pageUrl);
        startService(intent);


        ListView list = (ListView) findViewById(R.id.endless_list);

        //TODO:Make this fine
        adapter = new AuthorAdapter(this, ImageAuthorRegister.getInstance().getImages());

        list.setAdapter(adapter);

        //Create and initialize listener  - add author page for example
        EndlessScrollListener l = new EndlessScrollListener(this, AuthorLoader.class);
        l.putParam(AuthorLoader.PAGE_URL, pageUrl);
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
        actionBar.setIcon(R.drawable.icon_v2_small);

        //Get author from intent
        String name = getIntent().getExtras().getString(NAME);

        //Change title
        actionBar.setTitle(name);


        registerReceiver();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(AuthorLoader.AUTHOR_PAGE_LOADED));
        super.onResume();
    }

    private void registerReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (AuthorLoader.AUTHOR_PAGE_LOADED.equals(action)) {
                    adapter.addAll(ImageAuthorRegister.getInstance().getImages());
                    adapter.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(AuthorLoader.AUTHOR_PAGE_LOADED));

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
