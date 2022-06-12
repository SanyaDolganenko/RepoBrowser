package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.interfaces.IReposRepository
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import java.util.*


class SaveClickedRepoUseCase(
    private val reposRepository: IReposRepository,
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(clicked: RepositoryModel, readDate: Date) {
        val currentUser = usersRepository.getActiveUser() ?: return
        reposRepository.insertRead(clicked, currentUser.id, readDate.time)
    }
}