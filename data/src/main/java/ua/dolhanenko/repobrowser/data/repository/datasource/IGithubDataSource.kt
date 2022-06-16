package ua.dolhanenko.repobrowser.data.repository.datasource

import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IUserModel


interface IGithubDataSource {
    suspend fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null
    ): Resource<IFilteredRepositoriesModel?>

    suspend fun queryUserInfo(userToken: String): Resource<IUserModel?>
}