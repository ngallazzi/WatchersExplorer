package com.ngallazzi.watchersexplorer.models

data class WatchersResponse(var watchers: List<Owner>, var currentPageIndex: Int, var totalPagesCount: Int)