package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


interface IRepositoriesCacheDataSource {
    fun getItems(byUserId: Long): List<RepositoryModel>

    fun insert(repository: RepositoryModel, forUserId: Long)

    fun delete(repository: RepositoryModel)

    fun deleteAll()
}