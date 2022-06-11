package ua.dolhanenko.repobrowser.view.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dolhanenko.repobrowser.application.ModulesManager
import ua.dolhanenko.repobrowser.domain.usecases.*
import ua.dolhanenko.repobrowser.view.browse.BrowseVM
import ua.dolhanenko.repobrowser.view.login.LoginVM


class ModuledViewModelFactory(private val modules: ModulesManager) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginVM::class.java) -> LoginVM(
                LoginUseCase(), QueryUserInfoUseCase(modules.githubDataSource),
                SaveActiveUserUseCase(modules.usersCacheDataSource),
                GetActiveUserUseCase(modules.usersCacheDataSource)
            )
            modelClass.isAssignableFrom(BrowseVM::class.java) -> BrowseVM(
                FilterRepositoriesUseCase(modules.githubRepository)
            )
            else -> super.create(modelClass)
        } as T
    }
}