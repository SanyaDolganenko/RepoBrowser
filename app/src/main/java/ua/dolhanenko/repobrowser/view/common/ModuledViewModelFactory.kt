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
                LogoutUseCase(modules.usersRepository)
            )
            modelClass.isAssignableFrom(LoginVM::class.java) -> LoginVM(
                LoginUseCase(), QueryUserInfoUseCase(modules.usersRepository),
                SaveActiveUserUseCase(modules.usersRepository),
                GetActiveUserUseCase(modules.usersRepository)
            )
            modelClass.isAssignableFrom(BrowseVM::class.java) -> BrowseVM(
                FilterReposUseCase(modules.reposRepository),
                SaveClickedRepoUseCase(modules.reposRepository, modules.usersRepository),
                GetCachedReposUseCase(modules.reposRepository, modules.usersRepository)
            )
            modelClass.isAssignableFrom(HistoryVM::class.java) -> HistoryVM(
                GetCachedReposUseCase(modules.reposRepository, modules.usersRepository)
            )
            else -> super.create(modelClass)
        } as T
    }
}