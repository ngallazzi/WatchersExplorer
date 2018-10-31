package com.ngallazzi.watchersexplorer.remote

import com.ngallazzi.watchersexplorer.BuildConfig
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    fun listRepositories(@Query("q") key: String,
                         @Query("page") page: Int = 1): Observable<Result<PaginatedResponse>>

    companion object {
        private fun create(): GithubApi {

            val interceptor = HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            MoshiConverterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .build()

            return retrofit.create(GithubApi::class.java)
        }

        val gitHubApiServe by lazy {
            GithubApi.create()
        }
    }


}


