package ua.dolhanenko.repobrowser.data.remote.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ua.dolhanenko.repobrowser.data.remote.entity.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse


interface GithubApi {
    enum class SortingField(val value: String) {
        STARS("stars")
    }

    enum class OrderDirection(val value: String) {
        ASCENDING("asc"),
        DESCENDING("desc")
    }

    @GET("search/repositories")
    @Headers("Content-Type: application/json", "Accept: application/vnd.github.v3+json")
    fun getAllRepositories(
        @Query("per_page") pageSize: Int = 15,
        @Query("page") page: Int,
        @Query("q", encoded = true) query: String?,
        @Query("sort") sortBy: String = SortingField.STARS.value,
        @Query("order") order: String = OrderDirection.DESCENDING.value,
    ): Deferred<Response<FilteredReposResponse>>

    @GET("user")
    @Headers("Content-Type: application/json", "Accept: application/vnd.github.v3+json")
    fun getUserInfo(): Deferred<Response<UserResponse>>
}