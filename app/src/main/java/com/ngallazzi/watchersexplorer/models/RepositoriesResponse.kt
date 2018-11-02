package com.ngallazzi.watchersexplorer.models

import com.squareup.moshi.Json

class RepositoriesResponse {
    @Json(name = "total_count")
    var itemsCount: Int = 0
    @Json(name = "items")
    lateinit var items: List<Repository>
}