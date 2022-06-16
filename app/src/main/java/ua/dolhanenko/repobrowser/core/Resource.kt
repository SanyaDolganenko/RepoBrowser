package ua.dolhanenko.repobrowser.core


sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val exception: Throwable) : Resource<T>()
}