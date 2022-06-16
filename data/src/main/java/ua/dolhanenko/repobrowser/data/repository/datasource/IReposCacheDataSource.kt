package ua.dolhanenko.repobrowser.data.repository.datasource

import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel


internal interface IReposCacheDataSource {
    fun getItems(byUserId: Long): List<IRepositoryModel>

    fun insert(repository: IRepositoryModel, forUserId: Long, viewedAt: Long)

    fun delete(repository: IRepositoryModel)

    fun deleteAll()
}