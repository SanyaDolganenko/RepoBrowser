package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.IUserModel


class GetActiveUserUseCase(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(): IUserModel? {
        return usersRepository.getActiveUser()
    }
}