package ua.dolhanenko.repobrowser.data.remote.base

import retrofit2.Response
import ua.dolhanenko.repobrowser.core.NetworkException
import ua.dolhanenko.repobrowser.core.Resource


abstract class BaseApiDataSource {
    protected fun <T> Response<*>.toErrorResource(): Resource.Error<T> {
        return Resource.Error(NetworkException(code(), errorBody()?.string() ?: ""))
    }
}