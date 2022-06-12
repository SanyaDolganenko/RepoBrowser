package ua.dolhanenko.repobrowser.data.local.datasource

import ua.dolhanenko.repobrowser.data.local.dao.RepositoriesCacheDao
import ua.dolhanenko.repobrowser.data.local.entity.Repository
import ua.dolhanenko.repobrowser.data.repository.interfaces.IReposCacheDataSource


class RepositoriesCacheDataSource(private val dao: RepositoriesCacheDao) :
    IReposCacheDataSource {
    override fun getItems(byUserId: Long): List<Repository> {
        return dao.getItems(byUserId)
    }

    override fun insert(repository: Repository) {
        dao.insertAll(listOf(repository))
    }

    override fun delete(repository: Repository) {
        dao.delete(repository)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}