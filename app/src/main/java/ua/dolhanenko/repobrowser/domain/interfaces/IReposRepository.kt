package ua.dolhanenko.repobrowser.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.Resource


interface IReposRepository {
    fun getFreshFilteredPagesAsync(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<FilteredRepositoriesModel?>>

    fun getReadItems(byUserId: Long): List<RepositoryModel>

    fun insertRead(repository: RepositoryModel, forUserId: Long, viewedAt: Long)

    fun deleteRead(repository: RepositoryModel)
}