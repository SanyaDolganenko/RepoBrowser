package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class LogoutUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(): Boolean {
        val currentUser = usersRepository.getActiveUser() ?: return false
        usersRepository.deleteActiveUser(currentUser)
        usersRepository.putActiveToken(null)
        return true
    }
}