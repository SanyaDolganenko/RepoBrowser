package ua.dolhanenko.repobrowser.view.browse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.usecases.FilterRepositoriesUseCase
import ua.dolhanenko.repobrowser.utils.Constants
import java.util.concurrent.atomic.AtomicInteger


class BrowseVM(private val filterUseCase: FilterRepositoriesUseCase) : ViewModel() {
    val filteredRepositories: MutableLiveData<List<RepositoryModel>?> = MutableLiveData()
    val filteredFound: MutableLiveData<Long?> = MutableLiveData()
    val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var lastLoadedPage = AtomicInteger(0)
    private var lastFilter: String? = null

    fun onPageEndReached() {
        if (lastFilter.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        Log.d("BROWSE_VM", "Page end reached, loading more...")
        val lastLoaded = lastLoadedPage.get() + 1
        val ids =
            (lastLoaded until lastLoaded + Constants.PAGES_PER_ASYNC_LOAD).toList().toIntArray()
        startLoadingPages(lastFilter!!, ids)
    }

    fun onFilterInput(filter: String?) {
        lastFilter = filter
        lastLoadedPage.set(0)
        if (filter.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        isDataLoading.postValue(true)
        val ids = (1..Constants.PAGES_PER_ASYNC_LOAD).toList().toIntArray()
        startLoadingPages(filter, ids)
    }

    private fun startLoadingPages(filter: String, pageNumbers: IntArray) {
        viewModelScope.launch(Dispatchers.IO) {
            filterUseCase.invoke(filter, pageNumbers).collect {
                isDataLoading.postValue(false)
                lastLoadedPage.getAndIncrement()
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            appendRepositoriesForDisplay(it.pageNumber, it.items)
                            filteredFound.postValue(it.foundInTotal)
                        }
                    }
                    is Resource.Error -> {
                        Log.e("BROWSE_VM", "Encountered status code: ${it.exception.message}")
                        it.exception.printStackTrace()
                    }
                }
            }
        }
    }

    private fun appendRepositoriesForDisplay(number: Int, page: List<RepositoryModel>) {
        viewModelScope.launch(Dispatchers.Main) {
            val current = filteredRepositories.value?.toMutableList() ?: mutableListOf()
            Log.d(
                "BROWSE_VM",
                "Current size: ${current.size}. Appending page #$number ${page.size}"
            )
            current.addAll(page)
            filteredRepositories.value = current
        }
    }
}