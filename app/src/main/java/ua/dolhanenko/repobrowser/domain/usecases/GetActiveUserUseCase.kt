package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.UserModel
import ua.dolhanenko.repobrowser.domain.model.toModel


class GetActiveUserUseCase(private val usersCacheDataSource: IUsersCacheDataSource) {
    suspend operator fun invoke(): UserModel? {
        return usersCacheDataSource.getActiveUser()?.toModel()
    }
}