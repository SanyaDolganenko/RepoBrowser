package ua.dolhanenko.repobrowser.view.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dolhanenko.repobrowser.application.ModulesManager
import ua.dolhanenko.repobrowser.domain.usecases.*
import ua.dolhanenko.repobrowser.view.browse.BrowseVM
import ua.dolhanenko.repobrowser.view.history.HistoryVM
import ua.dolhanenko.repobrowser.view.host.HostVM
import ua.dolhanenko.repobrowser.view.login.LoginVM


class ModuledViewModelFactory(private val modules: ModulesManager) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HostVM::class.java) -> HostVM(
                LogoutUseCase(modules.usersCacheDataSource)
            )
            modelClass.isAssignableFrom(LoginVM::class.java) -> LoginVM(
                LoginUseCase(), QueryUserInfoUseCase(modules.githubDataSource),
                SaveActiveUserUseCase(modules.usersCacheDataSource),
                GetActiveUserUseCase(modules.usersCacheDataSource)
            )
            modelClass.isAssignableFrom(BrowseVM::class.java) -> BrowseVM(
                FilterReposUseCase(modules.githubRepository),
                SaveClickedRepoUseCase(
                    modules.repositoriesCacheDataSource,
                    modules.usersCacheDataSource
                ),
                GetCachedReposUseCase(
                    modules.repositoriesCacheDataSource,
                    modules.usersCacheDataSource
                )
            )
            modelClass.isAssignableFrom(HistoryVM::class.java) -> HistoryVM(
                GetCachedReposUseCase(
                    modules.repositoriesCacheDataSource,
                    modules.usersCacheDataSource
                )
            )
            else -> super.create(modelClass)
        } as T
    }
}