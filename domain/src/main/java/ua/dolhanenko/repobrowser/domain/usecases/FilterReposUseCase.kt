package ua.dolhanenko.repobrowser.domain.usecases

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import javax.inject.Inject


class FilterReposUseCase @Inject constructor(private val reposRepository: IReposRepository) {
    operator fun invoke(
        filter: String,
        startPage: Int,
        endPage: Int
    ): Flow<Resource<IFilteredRepositoriesModel?>> {
        return reposRepository.downloadFilteredPages(filter, startPage, endPage)
    }
}