package ua.dolhanenko.repobrowser.view.browse

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.usecases.FilterReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.SaveClickedRepoUseCase
import ua.dolhanenko.repobrowser.utils.Constants
import ua.dolhanenko.repobrowser.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.utils.toUri
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class BrowseVM(
    private val filterUseCase: FilterReposUseCase,
    private val saveClickedRepoUseCase: SaveClickedRepoUseCase,
    private val getCachedReposUseCase: GetCachedReposUseCase
) : ViewModel() {
    private val filteredRepositories: MutableLiveData<List<IRepositoryModel>?> = MutableLiveData()
    private val filteredFound: MutableLiveData<Long?> = MutableLiveData()
    private val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val viewUrlEvent: SingleLiveEvent<Uri?> = SingleLiveEvent()

    private var lastLoadedPage = AtomicInteger(0)
    private var lastFilter: String? = null
    private var readItems: List<IRepositoryModel> = mutableListOf()

    init {
        loadCachedRepos()
    }

    fun getFilteredRepositories(): LiveData<List<IRepositoryModel>?> = filteredRepositories
    fun getFilteredFound(): LiveData<Long?> = filteredFound
    fun getIsDataLoading(): LiveData<Boolean> = isDataLoading
    fun getViewUrlEvent(): LiveData<Uri?> = viewUrlEvent

    fun onRepositoryClick(model: IRepositoryModel, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val readDate = Date()
            saveClickedRepoUseCase(model, readDate)
            (readItems as MutableList).add(model)
            updateLocalRepositoryModel(position) {
                it.readAt = readDate
            }
            runOnUiThread {
                viewUrlEvent.value = model.url?.toUri()
            }
        }
    }

    fun onPageEndReached() {
        if (lastFilter.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        Log.d("BROWSE_VM", "Page end reached, loading more...")
        val lastLoaded = lastLoadedPage.get() + 1
        val ids =
            (lastLoaded until lastLoaded + Constants.PAGES_PER_ASYNC_LOAD).toList().toIntArray()
        startLoadingPages(lastFilter!!, ids, false)
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
        startLoadingPages(filter, ids, true)
    }

    private fun startLoadingPages(
        filter: String,
        pageNumbers: IntArray,
        startListFromScratch: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            filterUseCase.invoke(filter, pageNumbers).collect {
                isDataLoading.postValue(false)
                lastLoadedPage.getAndIncrement()
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            if (it.pageNumber > pageNumbers.first() || startListFromScratch.not()) {
                                appendRepositoriesForDisplay(it.pageNumber, it.items)
                            } else if (it.pageNumber == pageNumbers.first()) {
                                postRepositoriesForDisplay(it.pageNumber, it.items)
                            }
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

    private fun loadCachedRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            getCachedReposUseCase()?.toMutableList()?.let {
                readItems = it
            }
        }
    }

    private fun appendRepositoriesForDisplay(number: Int, page: List<IRepositoryModel>) {
        viewModelScope.launch(Dispatchers.Main) {
            val current = filteredRepositories.value?.toMutableList() ?: mutableListOf()
            Log.d(
                "BROWSE_VM",
                "Current size: ${current.size}. Appending page #$number ${page.size}"
            )
            page.markReadItems()
            current.addAll(page)
            filteredRepositories.value = current
        }
    }

    private fun postRepositoriesForDisplay(number: Int, page: List<IRepositoryModel>) {
        Log.d(
            "BROWSE_VM",
            "Rewriting repositories with page #$number ${page.size}"
        )
        page.markReadItems()
        filteredRepositories.postValue(page)
    }

    private fun updateLocalRepositoryModel(position: Int, block: (IRepositoryModel) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            val current = filteredRepositories.value?.toMutableList() ?: mutableListOf()
            if (position in current.indices) {
                val copy = current[position].clone()
                block(copy)
                current[position] = copy
                filteredRepositories.value = current
            }
        }
    }

    private fun List<IRepositoryModel>.markReadItems() {
        forEach { model ->
            readItems.find { it.id == model.id }?.let {
                model.readAt = it.readAt
            }
        }
    }
}