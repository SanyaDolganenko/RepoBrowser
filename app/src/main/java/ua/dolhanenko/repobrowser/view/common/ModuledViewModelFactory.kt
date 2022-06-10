package ua.dolhanenko.repobrowser.view.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dolhanenko.repobrowser.application.ModulesManager
import ua.dolhanenko.repobrowser.domain.usecases.FilterRepositoriesUseCase
import ua.dolhanenko.repobrowser.view.browse.BrowseVM


class ModuledViewModelFactory(private val modules: ModulesManager) :

    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BrowseVM::class.java) -> BrowseVM(
                FilterRepositoriesUseCase(modules.githubDataSource)
            )
            else -> super.create(modelClass)
        } as T
    }
}