package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.UserModel


class QueryUserInfoUseCase(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(userToken: String): Resource<UserModel?> {
        return usersRepository.queryFreshUserInfo(userToken)
    }
}