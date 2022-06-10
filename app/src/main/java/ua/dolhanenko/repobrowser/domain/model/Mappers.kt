package ua.dolhanenko.repobrowser.domain.model

import ua.dolhanenko.repobrowser.data.remote.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.RepoResponse


fun RepoResponse.toModel(): RepositoryModel {
    return RepositoryModel(id, name)
}

fun FilteredReposResponse.toModel(pageNumber: Int): FilteredRepositoriesModel {
    return FilteredRepositoriesModel(pageNumber, total_count, items.map { it.toModel() })
}