package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.UserModel


class SaveActiveUserUseCase(private val usersCacheDataSource: IUsersCacheDataSource) {
    suspend operator fun invoke(user: UserModel) {
        usersCacheDataSource.saveActiveUser(user)
    }
}