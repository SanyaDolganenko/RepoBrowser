package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


class GetCachedReposUseCase(
    private val cacheDataSource: IRepositoriesCacheDataSource,
    private val usersCacheDataSource: IUsersCacheDataSource
) {
    suspend operator fun invoke(): List<RepositoryModel>? {
        val currentUser = usersCacheDataSource.getActiveUser() ?: return null
        return cacheDataSource.getItems(currentUser.id)
    }
}