package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.RepoApp
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class LogoutUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(): Boolean {
        val currentUser = usersRepository.getActiveUser() ?: return false
        usersRepository.deleteActiveUser(currentUser)
        RepoApp.activeToken = null
        return true
    }
}