package com.ngallazzi.watchersexplorer

import android.R.attr.duration
import android.app.SearchManager
import android.content.Context
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.disposable
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.gitHubApiServe
import com.ngallazzi.watchersexplorer.remote.PaginatedResponse
import com.ngallazzi.watchersexplorer.remote.models.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_repositories.*


class SearchRepositoriesActivity : AppCompatActivity() {

    var repositories: ArrayList<Repository> = ArrayList()
    lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)
        // Verify the action and get the query
        if (ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                searchRepositories(query)
                this.query = query
            }
        }
        rvRepositories.layoutManager = LinearLayoutManager(this)
        rvRepositories.adapter = RepositoryAdapter(repositories, this)
    }

    fun searchRepositories(key: String) {
        disposable = gitHubApiServe.listRepositories(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            when {
                                result.itemsCount > 0 -> {
                                    rvRepositories.visibility = View.VISIBLE
                                    tvHint.visibility = View.GONE
                                    showRepos(result)
                                }
                                else -> {
                                    tvHint.visibility = View.VISIBLE
                                    rvRepositories.visibility = View.GONE
                                    showError(error = getString(R.string.no_repositories_found))
                                }
                            }
                        },
                        { error -> showError(error = error.message!!) }
                )
    }

    private fun showRepos(response: PaginatedResponse) {
        repositories.clear()
        for (item in response.items) {
            repositories.add(item)
        }
    }

    private fun showError(error: String) {
        Snackbar.make(clContainer, error, duration)
                .setAction(getString(R.string.retry)) { searchRepositories(query) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            requestFocusFromTouch()
        }

        return true
    }

    companion object {
        val TAG = SearchRepositoriesActivity::class.simpleName
    }
}
