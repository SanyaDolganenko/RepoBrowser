package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.UserModel


interface IGithubDataSource {
    suspend fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null
    ): Resource<FilteredRepositoriesModel?>

    suspend fun queryUserInfo(userToken: String): Resource<UserModel?>
}