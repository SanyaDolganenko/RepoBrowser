package ua.dolhanenko.repobrowser.presentation.history

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.presentation.base.BaseVM
import ua.dolhanenko.repobrowser.presentation.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.presentation.utils.toUri
import javax.inject.Inject

@HiltViewModel
class HistoryVM @Inject constructor(
    private val getCachedReposUseCase: GetCachedReposUseCase
) : BaseVM() {
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