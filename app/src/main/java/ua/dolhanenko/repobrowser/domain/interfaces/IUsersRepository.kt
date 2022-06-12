package ua.dolhanenko.repobrowser.domain.interfaces

import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.UserModel


interface IUsersRepository {
    suspend fun queryFreshUserInfo(userToken: String): Resource<UserModel?>

    fun getActiveUser(): UserModel?

    fun saveActiveUser(model: UserModel)

    fun deleteActiveUser(model: UserModel)
}