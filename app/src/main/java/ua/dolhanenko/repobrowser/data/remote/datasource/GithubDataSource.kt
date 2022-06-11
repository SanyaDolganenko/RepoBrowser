package ua.dolhanenko.repobrowser.data.remote.datasource

import ua.dolhanenko.repobrowser.data.remote.api.GithubApi
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.toModel


class GithubDataSource(private val api: GithubApi) : BaseApiDataSource(), IGithubDataSource {
    override suspend fun browseRepositories(
        limit: Int,
        page: Int,
        search: String?
    ): Resource<FilteredRepositoriesModel?> {
        val response = api.getAllRepositories(
            limit,
            page,
            search?.toNullIfEmpty()?.toSearchQuery()
        ).await()
        return if (response.isSuccessful) {
            Resource.Success(response.body()?.toModel(page))
        } else {
            response.toErrorResource()
        }
    }

    override suspend fun queryUserInfo(): UserResponse? {
        return api.getUserInfo().performRequest()
    }


    private fun String.toSearchQuery(): String {
        return "$this in:name"
    }

    private fun String.toNullIfEmpty(): String? = ifEmpty { null }
}