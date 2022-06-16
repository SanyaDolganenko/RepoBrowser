package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class SaveActiveTokenUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    operator fun invoke(token: String?) {
        return usersRepository.putActiveToken(token)
    }
}