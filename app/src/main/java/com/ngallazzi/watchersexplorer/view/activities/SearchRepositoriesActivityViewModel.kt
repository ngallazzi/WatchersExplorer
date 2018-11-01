package com.ngallazzi.watchersexplorer.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.disposable
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.gitHubApiServe
import com.ngallazzi.watchersexplorer.remote.models.RepositoriesResponse
import com.ngallazzi.watchersexplorer.view.ui.SearchRepositoriesActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * WatchersExplorer
 * Created by Nicola on 10/31/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */

class SearchRepositoriesActivityViewModel : ViewModel() {
    var repositoriesResponse: MutableLiveData<RepositoriesResponse> = MutableLiveData()
    var showError: MutableLiveData<String> = MutableLiveData()

    fun getRepositories(query: String, pageIndex: Int): LiveData<RepositoriesResponse> {
        disposable = gitHubApiServe.listRepositories(query, pageIndex, SearchRepositoriesActivity.ITEMS_PER_PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> repositoriesResponse.value = result },
                        { error -> showError.postValue(error.message) }
                )

        return repositoriesResponse
    }
}

