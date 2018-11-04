package com.ngallazzi.watchersexplorer.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.adapters.WatcherAdapter
import com.ngallazzi.watchersexplorer.models.Owner
import com.ngallazzi.watchersexplorer.models.Repository
import com.ngallazzi.watchersexplorer.models.WatchersResponse
import com.ngallazzi.watchersexplorer.models.toReadableDate
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
    private lateinit var watchersLiveData: LiveData<WatchersResponse>


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

        watchersLiveData = mActivityViewModel.getWatchers()

        watchersLiveData.observe(this@RepositoryWatchersActivity, Observer {
            totalPages = it.totalPagesCount
            currentPageIndex = it.currentPageIndex
            updateUi(it)
        })

        rvWatchers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(rvLayoutManager) {
            override fun onLoadMore(page: Int, view: RecyclerView) {
                if (currentPageIndex < totalPages) {
                    currentPageIndex++
                    mActivityViewModel.loadWatchers(mRepository.owner.login, mRepository.name,
                            currentPageIndex, ITEMS_PER_PAGE)
                }
            }

        })

        mActivityViewModel.loadWatchers(mRepository.owner.login, mRepository.name,
                1, ITEMS_PER_PAGE)

        mActivityViewModel.getError().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
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
        tvLastUpdate.text = getString(R.string.last_update_date, repository.updatedAt?.toReadableDate())
        tvUrl.text = getString(R.string.url, repository.htmlUrl)
    }

    private fun fromHtml(html: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }


    companion object {
        const val ITEMS_PER_PAGE = 50
    }
}