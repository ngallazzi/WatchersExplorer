package com.ngallazzi.watchersexplorer.remote.models

import com.squareup.moshi.Json


class Owner(@Json(name = "login") val login: String,
            @Json(name = "avatar_url") var avatarUrl: String,
            @Json(name = "url") var url: String)