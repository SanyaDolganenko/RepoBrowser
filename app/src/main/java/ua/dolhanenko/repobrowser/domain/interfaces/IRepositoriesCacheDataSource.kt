package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.data.local.entity.Repository


interface IRepositoriesCacheDataSource {
    fun getItems(byUserId: String): List<Repository>

    fun insertAll(repositories: List<Repository>)

    fun delete(repository: Repository)

    fun deleteAll()
}