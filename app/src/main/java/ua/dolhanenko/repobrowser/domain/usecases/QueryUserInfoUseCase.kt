package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.UserModel


class QueryUserInfoUseCase(private val githubDataSource: IGithubDataSource) {
    suspend operator fun invoke(userToken: String): Resource<UserModel?> {
        return githubDataSource.queryUserInfo(userToken)
    }
}