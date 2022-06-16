package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.UsersCacheDao
import ua.dolhanenko.repobrowser.data.local.entity.toDbEntity
import ua.dolhanenko.repobrowser.data.repository.datasource.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.IUserModel


class UsersCacheDataSource(private val dao: UsersCacheDao) : IUsersCacheDataSource {
    override fun getActiveUser(): IUserModel? {
        return dao.getActiveUser()
    }

    override fun saveActiveUser(user: IUserModel) {
        dao.save(user.toDbEntity(true))
        dao.setOtherUsersAsInactive(user.id)
    }

    override fun deleteActiveUser(user: IUserModel) {
        dao.delete(user.toDbEntity(true))
    }
}