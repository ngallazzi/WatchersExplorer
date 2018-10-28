package com.ngallazzi.watchersexplorer.remote.models

import com.squareup.moshi.Json


data class Repository(@Json(name = "id") var id: String,
                 @Json(name = "name") var name: String,
                 @Json(name = "description") var description: String,
                 @Json(name = "url") val url: String,
                 @Json(name = "watchers") var watchers: Int,
                 @Json(name = "owner") var owner: Owner)