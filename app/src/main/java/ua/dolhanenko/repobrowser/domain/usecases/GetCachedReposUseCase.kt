package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IReposRepository
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


class GetCachedReposUseCase(
    private val reposRepository: IReposRepository,
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(): List<RepositoryModel>? {
        val currentUser = usersRepository.getActiveUser() ?: return null
        return reposRepository.getReadItems(currentUser.id)
    }
}