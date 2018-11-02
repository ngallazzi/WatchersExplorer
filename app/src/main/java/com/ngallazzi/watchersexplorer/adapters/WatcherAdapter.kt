package com.ngallazzi.watchersexplorer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ngallazzi.watchersexplorer.R
import com.ngallazzi.watchersexplorer.models.Owner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_watcher.view.*

/**
 * WatchersExplorer
 * Created by Nicola on 10/29/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */
class WatcherAdapter(private val watchers: ArrayList<Owner>, val context: Context) :
        RecyclerView.Adapter<WatcherAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return watchers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_watcher, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvWatcherName.text = watchers[position].login
        Picasso.get().load(watchers[position].avatarUrl).into(holder.ivWatcherImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivWatcherImage = view.ivWatcherImage
        val tvWatcherName = view.tvWatcherName
    }
}