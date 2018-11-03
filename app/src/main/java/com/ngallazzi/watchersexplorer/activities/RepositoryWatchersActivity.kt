package com.ngallazzi.watchersexplorer.activities

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.adapters.WatcherAdapter
import com.ngallazzi.watchersexplorer.models.Owner
import com.ngallazzi.watchersexplorer.models.Repository
import com.ngallazzi.watchersexplorer.models.WatchersResponse
import kotlinx.android.synthetic.main.activity_repository_watchers.*

/**
 * WatchersExplorer
 * Created by Nicola on 10/30/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */
class RepositoryWatchersActivity : AppCompatActivity() {
    private lateinit var mRepository: Repository
    private lateinit var mActivityViewModel: RepositoryWatchersActivityViewModel
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
    private var watchers: ArrayList<Owner> = ArrayList()
    private lateinit var rvLayoutManager: GridLayoutManager
    private var totalPages = 1
    private var currentPageIndex = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_watchers)
        mRepository = intent.getParcelableExtra(getString(R.string.repository_id))
        mActivityViewModel = ViewModelProviders.of(this).get(RepositoryWatchersActivityViewModel::class.java)
        setLayoutHeader(mRepository)

        rvLayoutManager = GridLayoutManager(this, 4)
        rvAdapter = WatcherAdapter(watchers, this)

        rvWatchers.apply {
            adapter = rvAdapter
            layoutManager = rvLayoutManager
        }

        rvWatchers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(rvLayoutManager) {
            override fun onLoadMore(page: Int, view: RecyclerView) {
                if (currentPageIndex < totalPages) {
                    currentPageIndex += 1
                    mActivityViewModel.getWatchers(mRepository.owner.login, mRepository.name,
                            currentPageIndex, ITEMS_PER_PAGE)
                }
            }

        })

        // Create the observer which updates the UI.
        mActivityViewModel.watchersResponse.observe(this, Observer { response ->
            totalPages = response.totalPagesCount
            currentPageIndex = response.currentPageIndex
            updateUi(response)
            Log.v(TAG, "Current page index: " + currentPageIndex)
            Log.v(TAG, "Total pages: " + totalPages)
            Log.v(TAG, "Items count: " + response.watchers.size)
        })

        mActivityViewModel.showError.observe(this, Observer {
            Toast.makeText(this@RepositoryWatchersActivity, it, Toast.LENGTH_SHORT).show()
        })

        mActivityViewModel.getWatchers(mRepository.owner.login, mRepository.name,
                currentPageIndex, ITEMS_PER_PAGE)
    }

    private fun updateUi(watchersResponse: WatchersResponse) {
        rvWatchers.visibility = View.VISIBLE
        for (item in watchersResponse.watchers) {
            watchers.add(item)
        }
        rvAdapter.notifyDataSetChanged()
    }

    private fun setLayoutHeader(repository: Repository) {
        tvName.text = repository.name
        tvDescription.text = repository.description
        tvLastUpdate.text = getString(R.string.last_update_date, repository.updatedAt)
        tvCloneUrl.text = getString(R.string.clone_url, Html.fromHtml(repository.cloneUrl))
    }

    companion object {
        const val ITEMS_PER_PAGE = 20
        val TAG = RepositoryWatchersActivity::class.java.simpleName
    }
}