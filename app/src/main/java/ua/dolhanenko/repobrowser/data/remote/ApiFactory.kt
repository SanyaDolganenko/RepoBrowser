package ua.dolhanenko.repobrowser.data.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dolhanenko.repobrowser.BuildConfig
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.data.remote.api.GithubApi
import java.util.concurrent.TimeUnit


class ApiFactory {
    private fun Request.Builder.setupAuth(): Request.Builder {
        addHeader(
            "Authorization",
            "token ${RepoApp.activeToken}"
        )
        return this
    }

    private val authenticationInterceptor = Interceptor { chain ->
        val newRequest = chain.request()
            .newBuilder()
            .setupAuth()
            .build()
        chain.proceed(newRequest)
    }

    private val requestClient = OkHttpClient().newBuilder()
        .addInterceptor(authenticationInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().create())
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val githubApi: GithubApi = retrofit(requestClient).create(GithubApi::class.java)
}