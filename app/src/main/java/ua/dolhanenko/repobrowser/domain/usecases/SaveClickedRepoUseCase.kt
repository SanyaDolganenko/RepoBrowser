package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


class SaveClickedRepoUseCase(
    private val cacheDataSource: IRepositoriesCacheDataSource,
    private val usersCacheDataSource: IUsersCacheDataSource
) {
    suspend operator fun invoke(clicked: RepositoryModel) {
        val currentUser = usersCacheDataSource.getActiveUser() ?: return
        cacheDataSource.insert(clicked, currentUser.id)
    }
}