package ua.dolhanenko.repobrowser.data.remote

import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource


class GithubDataSource(val api: GithubApi) : BaseApiRepository(), IGithubDataSource {
    override suspend fun browseRepositories(
        limit: Int,
        page: Int,
        search: String?
    ): FilteredReposResponse? {
        val response = api.getAllRepositories(
            limit,
            page,
            search?.toNullIfEmpty()?.toSearchQuery()
        ).await()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw NetworkException(response.code(), response.errorBody()?.string() ?: "")
        }
    }

    private fun String.toSearchQuery(): String {
        return "$this in:name"
    }

    private fun String.toNullIfEmpty(): String? = ifEmpty { null }
}