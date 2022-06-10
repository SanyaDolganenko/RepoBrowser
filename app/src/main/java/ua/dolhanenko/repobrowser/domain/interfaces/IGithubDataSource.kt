package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.data.remote.FilteredReposResponse


interface IGithubDataSource {
   suspend fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null
    ): FilteredReposResponse?
}