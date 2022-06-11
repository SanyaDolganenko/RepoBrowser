package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.domain.model.UserModel


interface IUsersCacheDataSource {
    fun getActiveUser(): UserModel?

    fun saveActiveUser(user: UserModel)

    fun deleteUser(user: UserModel)
}