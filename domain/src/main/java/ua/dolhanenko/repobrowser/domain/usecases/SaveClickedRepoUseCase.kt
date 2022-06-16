package ua.dolhanenko.repobrowser.domain.usecases

import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import java.util.*
import javax.inject.Inject


class SaveClickedRepoUseCase @Inject constructor(
    private val reposRepository: IReposRepository,
    private val usersRepository: IUsersRepository
) {
    suspend operator fun invoke(clicked: IRepositoryModel, readDate: Date) {
        val currentUser = usersRepository.getActiveUser() ?: return
        reposRepository.insertRead(clicked, currentUser.id, readDate.time)
    }
}