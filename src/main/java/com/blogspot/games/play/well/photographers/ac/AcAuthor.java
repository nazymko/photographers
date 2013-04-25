package com.blogspot.games.play.well.photographers.ac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.ImageAuthorCache;
import com.blogspot.games.play.well.photographers.adapter.AuthorAdapter;
import com.blogspot.games.play.well.photographers.services.AuthorLoader;

/**
 * User: patronus
 */
public class AcAuthor extends SherlockActivity {
    AuthorAdapter adapter;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_list);


        ListView list = (ListView) findViewById(R.id.endless_list);

        //TODO:Make this fine
        adapter = new AuthorAdapter(this, ImageAuthorCache.getInstance().getImages());
        list.setAdapter(adapter);

        registerReceiver();
    }

    private void registerReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (AuthorLoader.AUTHOR_PAGE_LOADED.equals(action)) {
                    adapter.addAll(ImageAuthorCache.getInstance().getImages());
                    adapter.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(AuthorLoader.AUTHOR_PAGE_LOADED));

    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
