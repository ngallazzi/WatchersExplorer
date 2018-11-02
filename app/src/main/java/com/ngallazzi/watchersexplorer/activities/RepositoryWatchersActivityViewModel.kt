package com.ngallazzi.watchersexplorer.activities


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ngallazzi.watchersexplorer.models.Owner
import com.ngallazzi.watchersexplorer.models.WatchersResponse
import com.ngallazzi.watchersexplorer.remote.repository.GithubApi
import com.ngallazzi.watchersexplorer.remote.repository.PaginationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Nicola on 2017-03-02.
 */

class RepositoryWatchersActivityViewModel() : ViewModel() {

    var watchersResponse: MutableLiveData<WatchersResponse> = MutableLiveData()
    var showError: MutableLiveData<String> = MutableLiveData()

    fun getWatchers(ownerName: String, repoName: String, pageIndex: Int, perPage: Int) {

        val service = GithubApi.gitHubApiServe
        val call = service.listWatchers(ownerName, repoName, pageIndex, perPage)
        call.enqueue(object : Callback<List<Owner>> {
            override fun onResponse(call: Call<List<Owner>>, response: Response<List<Owner>>) {
                if (response.isSuccessful) {
                    val headers = response.headers()
                    val totalPages = PaginationUtils.getTotalPagesCount(headers)
                    watchersResponse.value = WatchersResponse(response.body()!!, pageIndex, totalPages)
                } else {
                    showError.value = "An error occurred" // todo return a better message
                }
            }

            override fun onFailure(call: Call<List<Owner>>, t: Throwable) {
                showError.value = t.message
            }
        })
    }
}