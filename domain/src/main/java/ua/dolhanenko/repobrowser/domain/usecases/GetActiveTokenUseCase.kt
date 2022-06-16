package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class GetActiveTokenUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(): String? {
        return usersRepository.getActiveToken()
    }
}