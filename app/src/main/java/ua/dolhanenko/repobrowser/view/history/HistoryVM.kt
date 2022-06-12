package ua.dolhanenko.repobrowser.view.history

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.utils.toUri


class HistoryVM(private val getCachedReposUseCase: GetCachedReposUseCase) : ViewModel() {
    private val cachedRepositories: MutableLiveData<List<RepositoryModel>?> = MutableLiveData()
    private val viewUrlEvent: SingleLiveEvent<Uri?> = SingleLiveEvent()

    fun getCachedRepositories(): LiveData<List<RepositoryModel>?> = cachedRepositories
    fun getViewUrlEvent(): LiveData<Uri?> = viewUrlEvent

    fun onFragmentResume() {
        retrieveCachedRepositories()
    }

    fun onItemClick(model: RepositoryModel) {
        runOnUiThread {
            viewUrlEvent.value = model.url.toUri()
        }
    }

    private fun retrieveCachedRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedRepositories.postValue(getCachedReposUseCase())
        }
    }
}