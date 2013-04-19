package com.example.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import com.example.photographers.Image;
import com.example.photographers.util.Log;

/**
 * User: Andrew.Nazymko
 */
public class Consumer {


    IntentService context;

    public Consumer(IntentService context) {
        this.context = context;
    }

    public void send(Image img) {
        Intent intent = new Intent();
        intent.setAction(LazyLoader.NEW_ELEMENT);
        intent.putExtra(LazyLoader.ITEM, img);
//        Log.w("SENDING" + img);
        context.sendBroadcast(intent);
    }

}
