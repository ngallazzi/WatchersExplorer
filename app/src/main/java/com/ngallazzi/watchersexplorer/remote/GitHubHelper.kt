package com.ngallazzi.watchersexplorer.remote

import okhttp3.Headers

class GitHubHelper {

    fun getRepositories(key: String, pageIndex: Int) {
        GithubApi.gitHubApiServe.listRepositories(key, pageIndex)
                .observeOn()
    }


    companion object {
        fun getTotalPages(headers: Headers): Int {
            var pageCount = 1
            val link = headers.get("Link")
            if (link != null) {
                val elements = link!!.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val position = elements[1].lastIndexOf("page=")
                val page = elements[1].substring(position, elements[1].length - 1)
                pageCount = Integer.valueOf(page.replace("page=", ""))
            }
            return pageCount
        }
    }
}