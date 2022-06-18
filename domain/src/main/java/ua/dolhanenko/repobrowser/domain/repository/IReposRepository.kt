package ua.dolhanenko.repobrowser.domain.repository

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel


interface IReposRepository {
    /**
     * Download N pages async and receive them sorted, one by one in the flow.
     * @param startPage the first page that will be loaded async (inclusive).
     * @param endPage the last page that will be loaded async (inclusive).
     */
    fun downloadFilteredPages(
        filter: String,
        startPage: Int,
        endPage: Int
    ): Flow<Resource<IFilteredRepositoriesModel?>>

    fun getReadItems(byUserId: Long): List<IRepositoryModel>

    fun insertRead(repository: IRepositoryModel, forUserId: Long, viewedAt: Long)

    fun deleteRead(repository: IRepositoryModel)
}