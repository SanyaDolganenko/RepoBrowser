package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.IUserModel


class QueryUserInfoUseCase(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(userToken: String): Resource<IUserModel?> {
        return usersRepository.queryFreshUserInfo(userToken)
    }
}