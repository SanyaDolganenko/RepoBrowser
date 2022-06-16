package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.RepositoriesCacheDao
import ua.dolhanenko.repobrowser.data.local.entity.toDbEntity
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel


class ReposCacheDataSource(private val dao: RepositoriesCacheDao) : IReposCacheDataSource {
    override fun getItems(byUserId: Long): List<IRepositoryModel> {
        return dao.getItems(byUserId)
    }

    override fun insert(repository: IRepositoryModel, forUserId: Long, viewedAt: Long) {
        dao.insertAll(listOf(repository.toDbEntity(forUserId, viewedAt)))
    }

    override fun delete(repository: IRepositoryModel) {
        dao.delete(repository.toDbEntity(0))
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}