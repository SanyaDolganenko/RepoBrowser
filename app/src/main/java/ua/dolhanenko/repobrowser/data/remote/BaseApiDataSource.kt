package ua.dolhanenko.repobrowser.data.remote

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.concurrent.CancellationException


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

    protected fun <A> safeAsyncRequest(
        deferredResponse: Deferred<Response<A>>,
        callback: (A?, String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = deferredResponse.await()
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    val errorModel = response.errorBody()?.string()
                    callback(null, errorModel)

                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Log.e("NETWORK", "error: ${e.message}")
                    callback(null, e.message)
                }
            }
        }
    }

    class NetworkException(val code: Int, message: String) : Exception(message)
}