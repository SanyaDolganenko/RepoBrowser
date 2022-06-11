package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource


interface IGithubDataSource {
    suspend fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null
    ): Resource<FilteredRepositoriesModel?>

    suspend fun queryUserInfo(): UserResponse?
}