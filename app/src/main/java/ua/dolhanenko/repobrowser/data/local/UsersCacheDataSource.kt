package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.UsersCacheDao
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.UserModel
import ua.dolhanenko.repobrowser.domain.model.toDbEntity
import ua.dolhanenko.repobrowser.domain.model.toModel


class UsersCacheDataSource(private val dao: UsersCacheDao) : IUsersCacheDataSource {
    override fun getActiveUser(): UserModel? {
        return dao.getActiveUser()?.toModel()
    }

    override fun saveActiveUser(user: UserModel) {
        dao.save(user.toDbEntity(true))
        dao.setOtherUsersAsInactive(user.id)
    }

    override fun deleteUser(user: UserModel) {
        dao.delete(user.toDbEntity(true))
    }
}