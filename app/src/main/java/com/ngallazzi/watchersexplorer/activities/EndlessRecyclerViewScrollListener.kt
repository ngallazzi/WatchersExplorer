package com.ngallazzi.watchersexplorer.activities

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Nicola on 2017-03-06.
 */

abstract class EndlessRecyclerViewScrollListener(var layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
    // The current offset index of data you have loaded
    var currentPage = 1;
    // The total number of items in the dataset after the last load
    var previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    var loading = true;
    // Sets the starting page index
    var startingPageIndex = 1;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    var visibleThreshold = 12;

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var lastVisibleItemPosition = 0;
        var totalItemCount = layoutManager.getItemCount();

        if (layoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition();
        } else if (layoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount >= previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the VISIBLE_THRESHOLD and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, recyclerView);
            loading = true;
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, view: RecyclerView)
}

