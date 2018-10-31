package com.ngallazzi.watchersexplorer.remote.models

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.remote.GithubApi
import com.ngallazzi.watchersexplorer.remote.models.Repository
import com.ngallazzi.watchersexplorer.view.ui.SearchRepositoriesActivity
import com.squareup.moshi.Json
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_repositories.*

class RepositoriesResponse {
    @Json(name = "total_count")
    var itemsCount: Int = 0
    @Json(name = "items")
    lateinit var items: List<Repository>
}