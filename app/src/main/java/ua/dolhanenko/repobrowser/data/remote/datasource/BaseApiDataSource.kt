package ua.dolhanenko.repobrowser.data.remote.datasource

import kotlinx.coroutines.Deferred
import retrofit2.Response
import ua.dolhanenko.repobrowser.domain.model.Resource


open class BaseApiDataSource {
    companion object {
        const val CODE_UNAUTHORIZED = 401
    }

    protected suspend fun <A> Deferred<Response<A>>.performRequest(): A? {
        val response = await()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw NetworkException(response.code(), response.errorBody()?.string() ?: "")
        }
    }

    protected fun <T> Response<*>.toErrorResource(): Resource.Error<T> {
        return Resource.Error(NetworkException(code(), errorBody()?.string() ?: ""))
    }

    class NetworkException(val code: Int, message: String) : Exception(message)
}