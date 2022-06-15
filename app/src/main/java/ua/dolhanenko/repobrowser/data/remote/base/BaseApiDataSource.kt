package ua.dolhanenko.repobrowser.data.remote.base

import retrofit2.Response
import ua.dolhanenko.repobrowser.domain.model.Resource


abstract class BaseApiDataSource {
    companion object {
        const val CODE_UNAUTHORIZED = 401
    }

    protected fun <T> Response<*>.toErrorResource(): Resource.Error<T> {
        return Resource.Error(NetworkException(code(), errorBody()?.string() ?: ""))
    }

    class NetworkException(val code: Int, message: String) : Exception(message)
}