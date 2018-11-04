package com.ngallazzi.watchersexplorer.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.models.RepositoriesResponse
import com.ngallazzi.watchersexplorer.remote.repository.GithubApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * WatchersExplorer
 * Created by Nicola on 10/31/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */

class SearchRepositoriesActivityViewModel : ViewModel() {
    var repositoriesResponse: MutableLiveData<RepositoriesResponse> = MutableLiveData()
    var showError: MutableLiveData<String> = MutableLiveData()


    fun getRepositories(query: String, pageIndex: Int, itemsPerPage: Int) {
        val service = GithubApi.gitHubApiServe
        val call = service.listRepositories(query, pageIndex, itemsPerPage)
        call.enqueue(object : Callback<RepositoriesResponse> {
            override fun onResponse(call: Call<RepositoriesResponse>, response: Response<RepositoriesResponse>) {
                if (response.isSuccessful) {
                    repositoriesResponse.value = response.body()
                } else {
                    showError.value = response.errorBody()!!.string()
                }
            }

            override fun onFailure(call: Call<RepositoriesResponse>, t: Throwable) {
                showError.value = t.message
            }
        })
    }

}

