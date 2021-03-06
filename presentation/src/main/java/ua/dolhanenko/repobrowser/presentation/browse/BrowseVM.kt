package ua.dolhanenko.repobrowser.presentation.browse

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.core.Constants
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.FilterReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.SaveClickedRepoUseCase
import ua.dolhanenko.repobrowser.presentation.base.BaseVM
import ua.dolhanenko.repobrowser.presentation.utils.SingleLiveEvent
import ua.dolhanenko.repobrowser.presentation.utils.runOnUiThread
import ua.dolhanenko.repobrowser.presentation.utils.toUri
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
internal class BrowseVM @Inject constructor(
    private val filterUseCase: FilterReposUseCase,
    private val saveClickedRepoUseCase: SaveClickedRepoUseCase,
    private val getCachedReposUseCase: GetCachedReposUseCase
) : BaseVM() {
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
        logger.d("BROWSE_VM", "Page end reached, loading more...")
        val lastLoadedPage = lastLoadedPage.get()
        startLoadingPages(
            lastFilter!!,
            lastLoadedPage + 1,
            lastLoadedPage + Constants.PAGES_PER_ASYNC_LOAD,
            false
        )
    }

    fun onFilterInput(filter: String?) {
        lastFilter = filter
        lastLoadedPage.set(0)
        if (filter.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        isDataLoading.postValue(true)
        startLoadingPages(filter, 1, Constants.PAGES_PER_ASYNC_LOAD, true)
    }

    private fun startLoadingPages(
        filter: String,
        startPage: Int,
        lastPage: Int,
        startListFromScratch: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            filterUseCase.invoke(filter, startPage, lastPage).collect {
                isDataLoading.postValue(false)
                lastLoadedPage.getAndIncrement()
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            if (it.pageNumber > startPage || startListFromScratch.not()) {
                                appendRepositoriesForDisplay(it.pageNumber, it.items)
                            } else if (it.pageNumber == startPage) {
                                postRepositoriesForDisplay(it.pageNumber, it.items)
                            }
                            filteredFound.postValue(it.foundInTotal)
                        }
                    }
                    is Resource.Error -> {
                        logger.e("BROWSE_VM", "Encountered status code: ${it.exception.message}")
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
            logger.d(
                "BROWSE_VM",
                "Current size: ${current.size}. Appending page #$number ${page.size}"
            )
            page.markReadItems()
            current.addAll(page)
            filteredRepositories.value = current
        }
    }

    private fun postRepositoriesForDisplay(number: Int, page: List<IRepositoryModel>) {
        logger.d(
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