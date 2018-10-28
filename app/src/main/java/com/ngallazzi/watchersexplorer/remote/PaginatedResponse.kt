package com.ngallazzi.watchersexplorer.remote

import com.ngallazzi.watchersexplorer.remote.models.Repository
import com.squareup.moshi.Json

class PaginatedResponse {
    @Json(name = "total_count")
    var itemsCount: Int = 0
    @Json(name = "items")
    lateinit var items: List<Repository>
}