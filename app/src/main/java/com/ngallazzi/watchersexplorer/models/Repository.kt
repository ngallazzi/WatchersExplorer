package com.ngallazzi.watchersexplorer.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repository(@Json(name = "id") var id: String,
                      @Json(name = "name") var name: String,
                      @Json(name = "description") var description: String = "",
                      @Json(name = "url") val url: String = "",
                      @Json(name = "clone_url") val cloneUrl: String,
                      @Json(name = "updated_at") val updatedAt: String = "",
                      @Json(name = "watchers") var watchers: Int = 0,
                      @Json(name = "owner") var owner: Owner) : Parcelable