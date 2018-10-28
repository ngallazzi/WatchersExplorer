package com.ngallazzi.watchersexplorer

import android.R.attr.duration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.disposable
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.gitHubApiServe
import com.ngallazzi.watchersexplorer.remote.PaginatedResponse
import com.ngallazzi.watchersexplorer.remote.models.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_repositories.*


class SearchRepositoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)
        searchRepositories(KEY)
    }

    fun searchRepositories(key: String) {
        disposable = gitHubApiServe.listRepositories(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> showRepos(result) },
                        { error -> showError(error = error.message!!) }
                )
    }

    fun showRepos(response: PaginatedResponse) {
        for (repo in response.items) {
            Log.v(TAG, "Repository: " + repo.name)
        }
    }

    fun showError(error: String) {
        Snackbar.make(clContainer, error, duration)
                .setAction(getString(R.string.retry),
                        { searchRepositories(KEY) })
    }

    companion object {
        val TAG = SearchRepositoriesActivity::class.simpleName
        val KEY = "tripbook"
    }
}
