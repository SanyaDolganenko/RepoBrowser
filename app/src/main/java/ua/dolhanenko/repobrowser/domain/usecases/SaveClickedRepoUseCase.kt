package ua.dolhanenko.repobrowser.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


class SaveClickedRepoUseCase(private val cacheDataSource: IRepositoriesCacheDataSource) {
    suspend operator fun invoke(clicked: RepositoryModel) {
        val currentUser = (FirebaseAuth.getInstance().currentUser?.providerData as UserInfo).email

    }
}