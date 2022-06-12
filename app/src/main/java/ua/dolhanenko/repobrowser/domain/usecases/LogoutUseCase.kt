package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository


class LogoutUseCase(
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(): Boolean {
        val currentUser = usersRepository.getActiveUser() ?: return false
        usersRepository.deleteActiveUser(currentUser)
        RepoApp.activeToken = null
        return true
    }
}