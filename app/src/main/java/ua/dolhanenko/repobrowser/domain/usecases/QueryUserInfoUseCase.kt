package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.model.UserModel
import ua.dolhanenko.repobrowser.domain.model.toModel


class QueryUserInfoUseCase(private val githubDataSource: IGithubDataSource) {
    suspend operator fun invoke(): UserModel? {
        return githubDataSource.queryUserInfo()?.toModel()
    }
}