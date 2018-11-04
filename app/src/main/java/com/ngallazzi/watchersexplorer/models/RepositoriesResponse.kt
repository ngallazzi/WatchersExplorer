package com.ngallazzi.watchersexplorer.models

import com.squareup.moshi.Json

data class RepositoriesResponse(@Json(name = "total_count") var itemsCount: Int = 0,
                           @Json(name = "items") var items: List<Repository>) {

}