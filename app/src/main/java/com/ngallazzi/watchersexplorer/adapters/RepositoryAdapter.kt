package com.ngallazzi.watchersexplorer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.activities.RepositoryWatchersActivity
import com.ngallazzi.watchersexplorer.models.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_repository.view.*

/**
 * WatchersExplorer
 * Created by Nicola on 10/29/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */
class RepositoryAdapter(private val repositories: ArrayList<Repository>, val context: Context) :
        RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return repositories.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_repository, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRepositoryName.text = repositories[position].name
        holder.tvRepositoryOwner.text = repositories[position].owner.login
        Picasso.get().load(repositories[position].owner.avatarUrl).into(holder.ivOwnerImage)
        holder.clContainer.setOnClickListener({
            goToRepositoryDetailsActivity(repositories[position])
        })
    }

    fun goToRepositoryDetailsActivity(repository: Repository) {
        val intent = Intent(context, RepositoryWatchersActivity::class.java)
        intent.putExtra(context.getString(R.string.repository_id), repository)
        context.startActivity(intent)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clContainer = view.clContainer
        val tvRepositoryName = view.tvRepositoryName!!
        val tvRepositoryOwner = view.tvRepositoryOwner
        val ivOwnerImage = view.ivOwnerImage
    }
}

