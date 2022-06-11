package ua.dolhanenko.repobrowser.domain.usecases

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubRepository
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource


class FilterRepositoriesUseCase(private val githubRepository: IGithubRepository) {
    operator fun invoke(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<FilteredRepositoriesModel?>> {
        return githubRepository.getPagesAsync(filter, pageNumbers)
    }
}