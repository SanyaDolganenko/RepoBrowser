package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.toModel


class FilterRepositoriesUseCase(private val githubDataSource: IGithubDataSource) {
    suspend operator fun invoke(
        filter: String,
        pageNumber: Int,
        pageSize: Int
    ): FilteredRepositoriesModel? {
        return githubDataSource.browseRepositories(
            limit = pageSize,
            page = pageNumber,
            search = filter
        )?.toModel(pageNumber)
    }
}