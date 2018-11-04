package com.ngallazzi.watchersexplorer.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(@Json(name = "login") val login: String,
            @Json(name = "avatar_url") var avatarUrl: String,
            @Json(name = "url") var url: String) : Parcelable