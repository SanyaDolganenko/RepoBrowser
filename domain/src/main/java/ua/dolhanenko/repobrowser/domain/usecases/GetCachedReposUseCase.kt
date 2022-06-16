package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Inject


class GetCachedReposUseCase @Inject constructor(
    private val reposRepository: IReposRepository,
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(): List<IRepositoryModel>? {
        val currentUser = usersRepository.getActiveUser() ?: return null
        return reposRepository.getReadItems(currentUser.id)
    }
}