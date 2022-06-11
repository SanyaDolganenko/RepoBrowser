package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.data.remote.entity.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse


interface IGithubDataSource {
    suspend fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null
    ): FilteredReposResponse?

    suspend fun queryUserInfo(): UserResponse?
}