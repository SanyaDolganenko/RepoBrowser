package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.UserModel


class GetActiveUserUseCase(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(): UserModel? {
        return usersRepository.getActiveUser()
    }
}