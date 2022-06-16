package ua.dolhanenko.repobrowser.data.remote.api

import retrofit2.Retrofit


internal interface IApiFactory {
    fun createDefaultRetrofit(): Retrofit
}