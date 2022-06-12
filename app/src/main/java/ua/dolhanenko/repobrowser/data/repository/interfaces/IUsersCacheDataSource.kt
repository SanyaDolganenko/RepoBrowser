package ua.dolhanenko.repobrowser.data.repository.interfaces

import ua.dolhanenko.repobrowser.data.local.entity.AppUser


interface IUsersCacheDataSource {
    fun getActiveUser(): AppUser?

    fun saveActiveUser(user: AppUser)

    fun deleteUser(user: AppUser)
}