package ua.dolhanenko.repobrowser.data.remote.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dolhanenko.repobrowser.data.repository.datasource.IActiveTokenDataSource
import java.util.concurrent.TimeUnit


internal class ApiFactory(
    private val activeTokenDataSource: IActiveTokenDataSource,
    private val baseUrl: String
) :
    IApiFactory {
    private fun Request.Builder.setupAuth(): Request.Builder {
        addHeader(
            "Authorization",
            "token ${activeTokenDataSource.getActiveToken()}"
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
        .baseUrl(baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().create())
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    override fun createDefaultRetrofit(): Retrofit = retrofit(requestClient)
}