package com.ngallazzi.watchersexplorer.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.adapters.RepositoryAdapter
import com.ngallazzi.watchersexplorer.remote.models.Repository
import kotlinx.android.synthetic.main.activity_search_repositories.*


class SearchRepositoriesActivity : AppCompatActivity() {
    private lateinit var mActivityViewModel: SearchRepositoriesActivityViewModel
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
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
        mActivityViewModel = ViewModelProviders.of(this).get(SearchRepositoriesActivityViewModel::class.java)
        // Verify the action and get the query
        if (ACTION_SEARCH == intent.action) {
            resetSearchIndexes()
            startFirstSearch(intent)
        }

        rvLayoutManager = LinearLayoutManager(this)
        rvAdapter = RepositoryAdapter(repositories, this)

        rvRepositories.apply {
            layoutManager = rvLayoutManager
            adapter = rvAdapter
        }

        // Create the observer which updates the UI.
        mActivityViewModel.repositoriesResponse.observe(this, Observer { response ->
            updateUi(response.items)
        })

        mActivityViewModel.showError.observe(this, Observer {
            Toast.makeText(this@SearchRepositoriesActivity, it, Toast.LENGTH_SHORT).show()
        })

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
                        totalPagesCount = getTotalPagesCount(totalItemCount, visibleItemCount)
                        currentPageIndex += 1
                        mActivityViewModel.getRepositories(query, currentPageIndex)
                    }
                }
            }
        })

    }


    override fun onNewIntent(intent: Intent) {
        resetSearchIndexes()
        startFirstSearch(intent)
    }

    private fun resetSearchIndexes() {
        isLoading = false
        isLastPage = false
        totalPagesCount = 0
    }

    private fun startFirstSearch(intent: Intent) {
        intent.getStringExtra(SearchManager.QUERY)?.also { query ->
            this.query = query
            repositories.clear()
            mActivityViewModel.getRepositories(query, currentPageIndex)
        }
    }

    private fun updateUi(repositoriesList: List<Repository>) {
        tvHint.visibility = View.GONE
        rvRepositories.visibility = View.VISIBLE
        for (item in repositoriesList) {
            repositories.add(item)
        }
        rvRepositories.adapter?.notifyDataSetChanged()
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
