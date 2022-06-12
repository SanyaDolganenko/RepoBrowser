package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource


class LogoutUseCase(
    private val usersCacheDataSource: IUsersCacheDataSource
) {
    suspend operator fun invoke(): Boolean {
        val currentUser = usersCacheDataSource.getActiveUser() ?: return false
        usersCacheDataSource.deleteUser(currentUser)
        RepoApp.activeToken = null
        return true
    }
}