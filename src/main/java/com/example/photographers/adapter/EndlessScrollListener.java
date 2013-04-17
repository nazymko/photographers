package com.example.photographers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.AbsListView;
import com.example.photographers.services.LazyLoader;

public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 1;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private Activity context;

    public EndlessScrollListener(Activity context) {
        this.context = context;
    }


    public EndlessScrollListener(int visibleThreshold, Activity context) {
        this.visibleThreshold = visibleThreshold;
        this.context = context;
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

            Intent service = new Intent(context, LazyLoader.class);
            service.putExtra(LazyLoader.PAGE, currentPage + 1);
            context.startService(service);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}