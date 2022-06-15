package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel


class GetCachedReposUseCase(
    private val reposRepository: IReposRepository,
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(): List<IRepositoryModel>? {
        val currentUser = usersRepository.getActiveUser() ?: return null
        return reposRepository.getReadItems(currentUser.id)
    }
}