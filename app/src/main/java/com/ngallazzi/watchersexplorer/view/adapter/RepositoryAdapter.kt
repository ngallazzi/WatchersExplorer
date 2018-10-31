package com.ngallazzi.watchersexplorer.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.remote.models.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_repository.view.*

/**
 * WatchersExplorer
 * Created by Nicola on 10/29/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */
class RepositoryAdapter(private val repositories: ArrayList<Repository>, val context: Context) :
        RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_repository, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRepositoryName.text = repositories[position].name + " - " + position
        Picasso.get().load(repositories[position].owner.avatarUrl).into(holder.ivOwnerImage)
    }

    // Gets the number of repositories in the list
    override fun getItemCount(): Int {
        return repositories.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvRepositoryName = view.tvRepositoryName!!
    val ivOwnerImage = view.ivOwnerImage
}