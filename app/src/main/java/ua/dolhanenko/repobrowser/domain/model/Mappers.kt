package ua.dolhanenko.repobrowser.domain.model

import ua.dolhanenko.repobrowser.data.remote.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.Owner
import ua.dolhanenko.repobrowser.data.remote.RepoResponse


fun RepoResponse.toModel(): RepositoryModel {
    return RepositoryModel(
        id,
        name,
        description ?: "",
        stargazers_count ?: 0,
        watchers_count ?: 0, language, url ?: "",
        owner?.toModel() ?: OwnerModel("", "")
    )
}

fun FilteredReposResponse.toModel(pageNumber: Int): FilteredRepositoriesModel {
    return FilteredRepositoriesModel(pageNumber, total_count, items.map { it.toModel() })
}

fun Owner.toModel(): OwnerModel = OwnerModel(login, avatar_url)