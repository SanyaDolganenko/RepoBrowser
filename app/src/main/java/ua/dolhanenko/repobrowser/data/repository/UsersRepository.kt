package ua.dolhanenko.repobrowser.data.repository

import ua.dolhanenko.repobrowser.data.local.toDbEntity
import ua.dolhanenko.repobrowser.data.local.toModel
import ua.dolhanenko.repobrowser.data.repository.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.model.UserModel



class UsersRepository(
    private val githubDataSource: IGithubDataSource,
    private val usersCacheDataSource: IUsersCacheDataSource
) : IUsersRepository {
    override suspend fun queryFreshUserInfo(userToken: String): Resource<UserModel?> {
        return githubDataSource.queryUserInfo(userToken)
    }

    override fun getActiveUser(): UserModel? {
        return usersCacheDataSource.getActiveUser()?.toModel()
    }

    override fun saveActiveUser(model: UserModel) {
        usersCacheDataSource.saveActiveUser(model.toDbEntity(true))
    }

    override fun deleteActiveUser(model: UserModel) {
        usersCacheDataSource.deleteUser(model.toDbEntity(true))
    }
}