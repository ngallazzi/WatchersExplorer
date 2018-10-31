package com.ngallazzi.watchersexplorer.service.pagination

/**
 * WatchersExplorer
 * Created by Nicola on 10/30/2018.
 * Copyright © 2018 Zehus. All rights reserved.
 */

object PaginationUtils {
    fun getPageCount(itemsCount: Int, itemsPerPage: Int): Int {
        var pageCount = itemsCount / itemsPerPage
        if (itemsCount % itemsPerPage > 0) {
            pageCount++
        }
        return pageCount
    }
}
