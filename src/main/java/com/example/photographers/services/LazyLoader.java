package com.example.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import com.example.photographers.Cache;
import com.example.photographers.util.Log;

/**
 * User: patronus
 * <p/>
 * Special for photographers.com.ua site
 */
public class LazyLoader extends IntentService {

    public static final String PHOTOGRAPHERS_SUPPLY = "http://photographers.com.ua/pictures/days/30/?page=";
    public static final String PAGE = "PAGE";
    //Activity actions
    public static final String PAGE_LOADED = "PAGE_LOADED";
    public static final String FEW_ELEMENTS_ADDED = "FEW_ELEMENTS_ADDED";

    public LazyLoader(String name) {
        super(name);
    }


    public LazyLoader() {
        super("lazy.loader.service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Get required page from intent

        int page = intent.getExtras().getInt(PAGE);

        Log.d("service started, page" + page);
        //prepare url
        String pageUrl = PHOTOGRAPHERS_SUPPLY + page;

        CoreLoader loader = new CoreLoader(this);
        loader.Load(pageUrl, Cache.getInstance());


        Intent message = new Intent();
        //Send message for updating
        message.setAction(PAGE_LOADED);
        Log.d("Broadcast message send");
        sendBroadcast(message);
    }

}
