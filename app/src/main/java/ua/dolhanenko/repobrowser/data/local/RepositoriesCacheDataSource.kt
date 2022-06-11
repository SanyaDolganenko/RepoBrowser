package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.RepositoriesCacheDao
import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.toDbEntity
import ua.dolhanenko.repobrowser.domain.model.toModel


class RepositoriesCacheDataSource(private val dao: RepositoriesCacheDao) :
    IRepositoriesCacheDataSource {
    override fun getItems(byUserId: Long): List<RepositoryModel> {
        return dao.getItems(byUserId).map { it.toModel() }
    }

    override fun insert(repository: RepositoryModel, forUserId: Long) {
        dao.insertAll(listOf(repository.toDbEntity(forUserId)))
    }

    override fun delete(repository: RepositoryModel) {
        dao.delete(repository.toDbEntity(0))
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}