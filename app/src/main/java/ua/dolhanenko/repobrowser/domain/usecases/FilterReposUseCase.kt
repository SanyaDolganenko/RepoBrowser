package ua.dolhanenko.repobrowser.domain.usecases

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource


class FilterReposUseCase(private val reposRepository: IReposRepository) {
    operator fun invoke(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<IFilteredRepositoriesModel?>> {
        return reposRepository.getFreshFilteredPagesAsync(filter, pageNumbers)
    }
}