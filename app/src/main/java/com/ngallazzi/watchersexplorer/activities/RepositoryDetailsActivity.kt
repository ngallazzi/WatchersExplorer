package com.ngallazzi.watchersexplorer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.remote.models.Repository
import kotlinx.android.synthetic.main.activity_repository_details.*

/**
 * WatchersExplorer
 * Created by Nicola on 10/30/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */
class RepositoryDetailsActivity : AppCompatActivity() {
    private lateinit var mRepository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_details)
        mRepository = intent.getParcelableExtra(getString(R.string.repository_id))
        setLayoutHeader(mRepository)
    }

    private fun setLayoutHeader(repository: Repository) {
        tvName.text = repository.name
        tvDescription.text = repository.description
        tvLastUpdate.text = repository.updatedAt.toString()
        tvCloneUrl.text = repository.cloneUrl
    }
}