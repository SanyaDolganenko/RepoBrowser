package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.model.IUserModel
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class GetActiveUserUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    operator fun invoke(): IUserModel? {
        return usersRepository.getActiveUser()
    }
}