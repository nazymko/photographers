package com.blogspot.games.play.well.photographers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.AbsListView;
import com.blogspot.games.play.well.photographers.services.LazyLoader;
import com.blogspot.games.play.well.photographers.util.Log;

import java.util.HashMap;
import java.util.Set;


public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 10;
    private Class loaderClass;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private Activity context;
    private HashMap<String, String> otherParam = new HashMap<String, String>();

    public EndlessScrollListener(Activity context, Class loaderClass) {
        this.context = context;
        this.loaderClass = loaderClass;
    }

    public void putParam(String key, String value) {
        if (key != null && value != null) {
            otherParam.put(key, value);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            Intent service = new Intent(context, loaderClass);

            Log.w("Starting service:" + loaderClass);
            Set<String> strings = otherParam.keySet();
            for (String key : strings) {
                service.putExtra(key, otherParam.get(key));
            }

            service.putExtra(LazyLoader.PAGE, currentPage + 1);
            context.startService(service);
            loading = true;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}