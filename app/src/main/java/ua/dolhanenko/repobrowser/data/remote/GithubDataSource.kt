package ua.dolhanenko.repobrowser.data.remote

import ua.dolhanenko.repobrowser.data.remote.api.GithubApi
import ua.dolhanenko.repobrowser.data.remote.base.BaseApiDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IUserModel
import ua.dolhanenko.repobrowser.core.Resource


class GithubDataSource(private val api: GithubApi) : BaseApiDataSource(), IGithubDataSource {
    override suspend fun browseRepositories(
        limit: Int,
        page: Int,
        search: String?
    ): Resource<IFilteredRepositoriesModel?> {
        val response = api.getAllRepositories(
            limit,
            page,
            search?.toNullIfEmpty()?.toSearchQuery()
        ).await()
        return if (response.isSuccessful) {
            Resource.Success(response.body()?.apply { this.pageNumber = page })
        } else {
            response.toErrorResource()
        }
    }

    override suspend fun queryUserInfo(userToken: String): Resource<IUserModel?> {
        val response = api.getUserInfo().await()
        return if (response.isSuccessful) {
            Resource.Success(response.body()?.apply { this.lastUsedToken = userToken })
        } else {
            response.toErrorResource()
        }
    }

    private fun String.toSearchQuery(): String {
        return "$this in:name"
    }

    private fun String.toNullIfEmpty(): String? = ifEmpty { null }
}