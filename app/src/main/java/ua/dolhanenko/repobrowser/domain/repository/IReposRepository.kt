package ua.dolhanenko.repobrowser.domain.repository

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.core.Resource


interface IReposRepository {
    fun getFreshFilteredPagesAsync(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<IFilteredRepositoriesModel?>>

    fun getReadItems(byUserId: Long): List<IRepositoryModel>

    fun insertRead(repository: IRepositoryModel, forUserId: Long, viewedAt: Long)

    fun deleteRead(repository: IRepositoryModel)
}