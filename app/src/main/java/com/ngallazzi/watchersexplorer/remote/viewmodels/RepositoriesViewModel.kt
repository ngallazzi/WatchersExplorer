package com.ngallazzi.watchersexplorer.remote.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.remote.GithubApi
import com.ngallazzi.watchersexplorer.remote.models.RepositoriesResponse
import com.ngallazzi.watchersexplorer.view.ui.SearchRepositoriesActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * WatchersExplorer
 * Created by Nicola on 10/31/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */

class RepositoriesViewModel : ViewModel() {
    private lateinit var repositoriesResponse: MutableLiveData<RepositoriesResponse>

    fun getRepositories(query: String, pageIndex: Int): LiveData<RepositoriesResponse> {
        if (!::repositoriesResponse.isInitialized) {
            repositoriesResponse = MutableLiveData()
            loadRepositories(query, pageIndex)
        }
        return repositoriesResponse
    }

    private fun loadRepositories(query: String, pageIndex: Int) {
        GithubApi.disposable = GithubApi.gitHubApiServe.listRepositories(query, pageIndex, SearchRepositoriesActivity.ITEMS_PER_PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var totalPagesCount = getTotalPagesCount(it.itemsCount, SearchRepositoriesActivity.ITEMS_PER_PAGE_COUNT)
                    Log.v(SearchRepositoriesActivity.TAG, "Total pages count: " + totalPagesCount)
                    Log.v(SearchRepositoriesActivity.TAG, "Current page index " + pageIndex)
                    Log.v(SearchRepositoriesActivity.TAG, "Current page items count " + it.items.count())
                    var liveData: MutableLiveData<RepositoriesResponse> = MutableLiveData()
                    liveData.value = it
                    repositoriesResponse = liveData
                }
    }

    private fun getTotalPagesCount(totalItemsCount: Int, itemsPerPageCount: Int): Int {
        var pageCount = totalItemsCount / itemsPerPageCount
        if (totalItemsCount % itemsPerPageCount > 0) {
            pageCount++
        }
        return pageCount
    }
}

