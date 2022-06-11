package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.dao.UsersCacheDao
import ua.dolhanenko.repobrowser.data.local.entity.AppUser
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource


class UsersCacheDataSource(private val dao: UsersCacheDao) : IUsersCacheDataSource {
    override fun getActiveUser(): AppUser {
        return dao.getActiveUser()
    }

    override fun saveActiveUser(user: AppUser) {
        dao.save(user)
        dao.setOtherUsersAsInactive(user.id)
    }
    
    override fun deleteUser(user: AppUser) {
        dao.delete(user)
    }
}