package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.RepositoriesCacheDao
import ua.dolhanenko.repobrowser.data.local.entity.Repository
import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource


class RepositoriesCacheDataSource(private val dao: RepositoriesCacheDao) :
    IRepositoriesCacheDataSource {
    override fun getItems(byUserId: String): List<Repository> {
        return dao.getItems(byUserId)
    }

    override fun insertAll(repositories: List<Repository>) {
        dao.insertAll(repositories)
    }

    override fun delete(repository: Repository) {
        dao.delete(repository)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}