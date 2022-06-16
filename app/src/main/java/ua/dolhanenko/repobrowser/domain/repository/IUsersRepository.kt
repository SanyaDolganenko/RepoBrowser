package ua.dolhanenko.repobrowser.domain.repository

import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IUserModel


interface IUsersRepository {
    suspend fun queryFreshUserInfo(userToken: String): Resource<IUserModel?>

    fun getActiveUser(): IUserModel?

    fun saveActiveUser(model: IUserModel)

    fun deleteActiveUser(model: IUserModel)
}