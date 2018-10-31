package com.ngallazzi.watchersexplorer.view.ui

import android.R.attr.duration
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.disposable
import com.ngallazzi.watchersexplorer.remote.GithubApi.Companion.gitHubApiServe
import com.ngallazzi.watchersexplorer.remote.PaginatedResponse
import com.ngallazzi.watchersexplorer.remote.models.Repository
import com.ngallazzi.watchersexplorer.view.adapter.RepositoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_repositories.*


class SearchRepositoriesActivity : AppCompatActivity() {

    private var repositories: ArrayList<Repository> = ArrayList()
    private lateinit var rvLayoutManager: LinearLayoutManager
    private lateinit var query: String
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPagesCount: Int = 0
    private var currentPageIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)
        // Verify the action and get the query
        if (ACTION_SEARCH == intent.action) {
            resetSearchIndexes()
            startFirstSearch(intent)
        }
        rvLayoutManager = LinearLayoutManager(this)
        rvRepositories.layoutManager = rvLayoutManager
        rvRepositories.adapter = RepositoryAdapter(repositories, this)
        rvRepositories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isLastPage = currentPageIndex == totalPagesCount
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val visibleItemCount = rvLayoutManager.childCount
                val totalItemCount = rvLayoutManager.itemCount
                val firstVisibleItemPosition = rvLayoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= ITEMS_PER_PAGE_COUNT) {
                        currentPageIndex += 1
                        searchRepositories(currentPageIndex)
                    }
                }
            }
        })
    }

    private fun searchRepositories(pageIndex: Int) {
        isLoading = true
        disposable = gitHubApiServe.listRepositories(query, pageIndex, ITEMS_PER_PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            isLoading = false
                            totalPagesCount = getTotalPagesCount(it.itemsCount, ITEMS_PER_PAGE_COUNT)
                            Log.v(TAG, "Total pages count: " + totalPagesCount)
                            Log.v(TAG, "Current page index " + pageIndex)
                            Log.v(TAG, "Current page items count " + it.items.count())
                            when {
                                it.items.count() > 0 -> {
                                    rvRepositories.visibility = View.VISIBLE
                                    tvHint.visibility = View.GONE
                                    showRepos(it)
                                }
                                else -> {
                                    tvHint.visibility = View.VISIBLE
                                    rvRepositories.visibility = View.GONE
                                    showError(error = getString(R.string.no_repositories_found))
                                }
                            }
                        },
                        { error ->
                            isLoading = false
                            showError(error = error.message!!)
                        }
                )
    }

    private fun showRepos(response: PaginatedResponse) {
        for (item in response.items) {
            repositories.add(item)
        }
        rvRepositories.adapter?.notifyDataSetChanged()
        Log.v(TAG, "Repositories array size: " + repositories.size)
    }

    override fun onNewIntent(intent: Intent) {
        resetSearchIndexes()
        startFirstSearch(intent)
    }

    private fun resetSearchIndexes() {
        isLoading = false
        isLastPage = false
        totalPagesCount = 0
        totalPagesCount = 1
    }

    private fun startFirstSearch(intent: Intent) {
        intent.getStringExtra(SearchManager.QUERY)?.also { query ->
            this.query = query
            repositories.clear()
            searchRepositories(1)
        }
    }

    private fun showError(error: String) {
        Snackbar.make(clContainer, error, duration)
                .setAction(getString(R.string.retry)) {
                    repositories.clear()
                    searchRepositories(currentPageIndex)
                }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }

        return true
    }

    private fun getTotalPagesCount(totalItemsCount: Int, itemsPerPageCount: Int): Int {
        var pageCount = totalItemsCount / itemsPerPageCount
        if (totalItemsCount % itemsPerPageCount > 0) {
            pageCount++
        }
        return pageCount
    }

    companion object {
        val TAG = SearchRepositoriesActivity::class.simpleName
        const val ITEMS_PER_PAGE_COUNT = 20
    }
}
