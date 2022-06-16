package ua.dolhanenko.repobrowser.data.repository.datasource

import ua.dolhanenko.repobrowser.domain.model.IUserModel


internal interface IUsersCacheDataSource {
    fun getActiveUser(): IUserModel?

    fun saveActiveUser(user: IUserModel)

    fun deleteActiveUser(user: IUserModel)
}