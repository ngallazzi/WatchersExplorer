package com.ngallazzi.watchersexplorer.remote.repository

import com.ngallazzi.watchersexplorer.BuildConfig
import com.ngallazzi.watchersexplorer.models.Owner
import com.ngallazzi.watchersexplorer.models.RepositoriesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    fun listRepositories(@Query("q") key: String,
                         @Query("page") page: Int = 1,
                         @Query("per_page") perPage: Int = 20): Call<RepositoriesResponse>

    //https://api.github.com/repos/ngallazzi/tripbook/subscribers

    @GET("repos/{owner}/{repo}/subscribers")
    fun listWatchers(@Path("owner") ownerName: String,
                     @Path("repo") repoName: String,
                     @Query("page") page: Int = 1,
                     @Query("per_page") per_page: Int = 10): Call<List<Owner>>

    companion object {
        private fun create(): GithubApi {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            var client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(
                            MoshiConverterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .build()

            return retrofit.create(GithubApi::class.java)
        }


        val gitHubApiServe by lazy {
            create()
        }

    }


}


