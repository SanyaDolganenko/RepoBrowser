package ua.dolhanenko.repobrowser.view.browse

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import ua.dolhanenko.repobrowser.domain.usecases.FilterReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.GetCachedReposUseCase
import ua.dolhanenko.repobrowser.domain.usecases.SaveClickedRepoUseCase
import ua.dolhanenko.repobrowser.utils.Constants
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.utils.toUri
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class BrowseVM(
    private val filterUseCase: FilterReposUseCase,
    private val saveClickedRepoUseCase: SaveClickedRepoUseCase,
    private val getCachedReposUseCase: GetCachedReposUseCase
) : ViewModel() {
    val filteredRepositories: MutableLiveData<List<RepositoryModel>?> = MutableLiveData()
    val filteredFound: MutableLiveData<Long?> = MutableLiveData()
    val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var requestViewUrl: (Uri?) -> Unit = {}
    private var lastLoadedPage = AtomicInteger(0)
    private var lastFilter: String? = null
    private var readItems: List<RepositoryModel> = mutableListOf()

    init {
        loadCachedRepos()
    }

    fun onRepositoryClick(model: RepositoryModel, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val readDate = Date()
            saveClickedRepoUseCase(model, readDate)
            (readItems as MutableList).add(model)
            updateLocalRepositoryModel(position) {
                it.isRead = true
                it.readAt = readDate
            }
            runOnUiThread {
                requestViewUrl(model.url.toUri())
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

    private fun loadCachedRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            getCachedReposUseCase()?.toMutableList()?.let {
                readItems = it
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
            page.markReadItems()
            current.addAll(page)
            filteredRepositories.value = current
        }
    }

    private fun updateLocalRepositoryModel(position: Int, block: (RepositoryModel) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            val current = filteredRepositories.value?.toMutableList() ?: mutableListOf()
            if (position in current.indices) {
                val copy = current[position].copy()
                block(copy)
                current[position] = copy
                filteredRepositories.value = current
            }
        }
    }

    private fun List<RepositoryModel>.markReadItems() {
        forEach { model ->
            readItems.find { it.id == model.id }?.let {
                model.isRead = true
                model.readAt = it.readAt
            }
        }
    }
}