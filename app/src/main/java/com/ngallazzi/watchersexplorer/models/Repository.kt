package com.ngallazzi.watchersexplorer.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Repository(@Json(name = "id") var id: String,
                      @Json(name = "name") var name: String,
                      @Json(name = "description") var description: String?,
                      @Json(name = "url") val url: String?,
                      @Json(name = "clone_url") val cloneUrl: String?,
                      @Json(name = "html_url") val htmlUrl: String?,
                      @Json(name = "updated_at") val updatedAt: String?,
                      @Json(name = "watchers") var watchers: Int = 0,
                      @Json(name = "owner") var owner: Owner) : Parcelable

// Extension function to convert date in a readable format
fun String.toReadableDate(): String {
    val ORIGINAL_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val EXPECTED_DATE_FORMAT_PATTERN = "dd/MM/yyyy"
    val date = formatDate(this, ORIGINAL_DATE_FORMAT_PATTERN)
    val expectedFormat = SimpleDateFormat(EXPECTED_DATE_FORMAT_PATTERN, Locale.getDefault())
    return expectedFormat.format(date)
}

private fun formatDate(date: String, pattern: String): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    try {
        return formatter.parse(date)
    } catch (e: ParseException) {
        return null
    }

}