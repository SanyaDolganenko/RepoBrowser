package ua.dolhanenko.repobrowser.view.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase


class HistoryVM(private val getCachedReposUseCase: GetCachedReposUseCase) : ViewModel() {
    val cachedRepositories: MutableLiveData<List<RepositoryModel>?> = MutableLiveData()

    fun onFragmentResume() {
        retrieveCachedRepositories()
    }

    private fun retrieveCachedRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedRepositories.postValue(getCachedReposUseCase())
        }
    }
}