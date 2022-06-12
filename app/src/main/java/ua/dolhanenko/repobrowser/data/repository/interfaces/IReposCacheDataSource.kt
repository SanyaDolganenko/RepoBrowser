package ua.dolhanenko.repobrowser.data.repository.interfaces

import ua.dolhanenko.repobrowser.data.local.entity.Repository


interface IReposCacheDataSource {
    fun getItems(byUserId: Long): List<Repository>

    fun insert(repository: Repository)

    fun delete(repository: Repository)

    fun deleteAll()
}