package com.ayaz.instamart.marttool;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by Ayaz on 27.2.2017
 */

public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private LoadListener loadListener;
    private int visibleThreshold = 10;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = false;
    private int startingPageIndex = 0;



    public EndlessScrollListener(LoadListener loadListener){
        this.loadListener = loadListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("scroll", "true");
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            loading = loadListener.onRefresh(currentPage + 1);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public interface LoadListener{
        boolean onRefresh(int pageCount);
    };

}
