package ua.dolhanenko.repobrowser.view.browse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ua.dolhanenko.repobrowser.data.remote.BaseApiDataSource
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.usecases.FilterRepositoriesUseCase
import ua.dolhanenko.repobrowser.utils.Constants
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class BrowseVM(private val filterUseCase: FilterRepositoriesUseCase) : ViewModel() {
    val filteredRepositories: MutableLiveData<List<RepositoryModel>?> = MutableLiveData()
    val filteredFound: MutableLiveData<Long?> = MutableLiveData()
    val isDataLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var lastLoadedPage = AtomicInteger(0)
    private var lastFilter: String? = null

    fun onPageEndReached() {
        Log.d("BrowseVM", "Page end reached, loading more...")
        loadNextFilteredPagesAsync(lastFilter) {
            it?.let {
                val current = filteredRepositories.value?.toMutableList() ?: mutableListOf()
                current.addAll(it)
                filteredRepositories.postValue(current)
            }
        }

    }

    fun onFilterInput(filter: String?) {
        lastFilter = filter
        lastLoadedPage.set(0)
        isDataLoading.postValue(true)
        loadNextFilteredPagesAsync(filter) {
            filteredRepositories.postValue(it)
            isDataLoading.postValue(false)
        }
    }

    private fun loadNextFilteredPagesAsync(
        filter: String?,
        onLoaded: (List<RepositoryModel>?) -> Unit
    ) {
        filteredFound.postValue(null)
        if (filter.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val results = mutableListOf<Deferred<FilteredRepositoriesModel?>>()
            for (i in 1..Constants.PAGES_PER_ASYNC_LOAD) {
                val pageToLoad = lastLoadedPage.get() + i
                Log.d(
                    "BrowseVM",
                    "Preparing to load page #$pageToLoad"
                )
                results.add(async {
                    suspendCoroutine {
                        launch {
                            it.resume(loadFilteredPage(pageToLoad, Constants.PAGE_SIZE, filter))
                        }
                    }
                })
            }
            val loadedPages = results.awaitAll()
            if (loadedPages.filterNotNull().isNotEmpty()) {
                filteredFound.postValue(loadedPages.first()?.foundInTotal)
            }
            val combinedNewPages = loadedPages
                .sortedBy { it?.pageNumber }
                .flatMap { it?.items ?: listOf() }
            lastLoadedPage.set(lastLoadedPage.get() + loadedPages.filterNotNull().size)
            Log.d(
                "BrowseVM",
                "Loaded new combined pages with total size: ${combinedNewPages.size}"
            )
            onLoaded(combinedNewPages)
        }
    }

    private suspend fun loadFilteredPage(
        page: Int,
        pageSize: Int,
        filter: String
    ): FilteredRepositoriesModel? {
        return try {
            filterUseCase.invoke(filter, page, pageSize)
        } catch (e: BaseApiDataSource.NetworkException) {
            Log.e("BROWSE_VM", "Encountered status code: ${e.code}")
            e.printStackTrace()
            null
        }
    }
}