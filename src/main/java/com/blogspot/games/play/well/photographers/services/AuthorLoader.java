package com.blogspot.games.play.well.photographers.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * User: patronus
 */
public class AuthorLoader extends IntentService {


    public static final String AUTHOR_PAGE_LOADED = "AUTHOR_PAGE_LOADED";
    public static final String PAGE = "PAGE";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AuthorLoader(String name) {
        super(name);
    }

    public AuthorLoader() {
        super(AuthorLoader.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int page = intent.getIntExtra(PAGE, 0);



    }

    private void sendFinish() {

        Intent intent = new Intent(AUTHOR_PAGE_LOADED);
        sendBroadcast(intent);


    }
}
