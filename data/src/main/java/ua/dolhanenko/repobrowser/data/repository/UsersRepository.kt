package ua.dolhanenko.repobrowser.data.repository

import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.data.repository.datasource.IActiveTokenDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.IUserModel
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository


internal class UsersRepository(
    private val activeTokenDataSource: IActiveTokenDataSource,
    private val githubDataSource: IGithubDataSource,
    private val usersCacheDataSource: IUsersCacheDataSource
) : IUsersRepository {
    override suspend fun queryFreshUserInfo(userToken: String): Resource<IUserModel?> {
        return githubDataSource.queryUserInfo(userToken)
    }

    override fun getActiveUser(): IUserModel? {
        return usersCacheDataSource.getActiveUser()
    }

    override fun saveActiveUser(model: IUserModel) {
        usersCacheDataSource.saveActiveUser(model)
    }

    override fun deleteActiveUser(model: IUserModel) {
        usersCacheDataSource.deleteActiveUser(model)
    }

    override fun getActiveToken(): String? {
        return activeTokenDataSource.getActiveToken()
    }

    override fun putActiveToken(token: String?) {
        activeTokenDataSource.putActiveToken(token)
    }
}