package ua.dolhanenko.repobrowser.data.remote.api

import retrofit2.Retrofit


interface IApiFactory {
    fun createDefaultRetrofit(): Retrofit
}