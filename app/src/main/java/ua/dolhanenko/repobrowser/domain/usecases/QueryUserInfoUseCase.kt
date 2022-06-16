package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IUserModel
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class QueryUserInfoUseCase @Inject constructor(private val usersRepository: IUsersRepository) {
    suspend operator fun invoke(userToken: String): Resource<IUserModel?> {
        return usersRepository.queryFreshUserInfo(userToken)
    }
}