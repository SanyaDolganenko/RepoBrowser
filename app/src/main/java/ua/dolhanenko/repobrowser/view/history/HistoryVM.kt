package ua.dolhanenko.repobrowser.view.history

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.utils.toUri


class HistoryVM(private val getCachedReposUseCase: GetCachedReposUseCase) : ViewModel() {
    private val cachedRepositories: MutableLiveData<List<IRepositoryModel>?> = MutableLiveData()
    private val viewUrlEvent: SingleLiveEvent<Uri?> = SingleLiveEvent()

    fun getCachedRepositories(): LiveData<List<IRepositoryModel>?> = cachedRepositories
    fun getViewUrlEvent(): LiveData<Uri?> = viewUrlEvent

    fun onFragmentResume() {
        retrieveCachedRepositories()
    }

    fun onItemClick(model: IRepositoryModel) {
        viewUrlEvent.value = model.url?.toUri()
    }

    private fun retrieveCachedRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedRepositories.postValue(getCachedReposUseCase())
        }
    }
}