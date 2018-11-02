package com.ngallazzi.watchersexplorer.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.remote.repository.GithubApi.Companion.disposable
import com.ngallazzi.watchersexplorer.remote.repository.GithubApi.Companion.gitHubApiServe
import com.ngallazzi.watchersexplorer.models.RepositoriesResponse
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

    fun getRepositories(query: String, pageIndex: Int, itemsPerPage: Int) {
        disposable = gitHubApiServe.listRepositories(query, pageIndex, itemsPerPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> repositoriesResponse.value = result },
                        { error -> showError.postValue(error.message) }
                )

    }
}

