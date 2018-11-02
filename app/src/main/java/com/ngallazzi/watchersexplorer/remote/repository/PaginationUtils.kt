package com.ngallazzi.watchersexplorer.remote.repository

import okhttp3.Headers

/**
 * WatchersExplorer
 * Created by Nicola on 10/30/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */

object PaginationUtils {
    fun getTotalPagesCount(totalItemsCount: Int, itemsPerPageCount: Int): Int {
        var pageCount = totalItemsCount / itemsPerPageCount
        if (totalItemsCount % itemsPerPageCount > 0) {
            pageCount++
        }
        return pageCount
    }

    fun getTotalPagesCount(headers: Headers): Int {
        var pageCount = 1
        val link = headers.get("Link")
        if (link != null) {
            val elements = link.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val position = elements[1].lastIndexOf("page=")
            val page = elements[1].substring(position, elements[1].length - 1)
            pageCount = Integer.valueOf(page.replace("page=", ""))
        }
        return pageCount
    }
}
