package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.IUserModel
import javax.inject.Inject


class SaveActiveUserUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(user: IUserModel) {
        usersRepository.saveActiveUser(user)
    }
}