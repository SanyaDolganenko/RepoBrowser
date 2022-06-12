package ua.dolhanenko.repobrowser.data.remote.repository

import android.util.Log
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubRepository
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource
import java.util.concurrent.atomic.AtomicInteger

typealias UnpublishedPage = Pair<Int, Resource<FilteredRepositoriesModel?>>

class GithubRepository(private val pageSize: Int, private val datasource: IGithubDataSource) :
    IGithubRepository {
    private companion object {
        const val LOG_TAG = "GITHUB_REPO"
    }

    private val loadedPages: AtomicInteger = AtomicInteger(0)
    private var pageNumOffset: Int = 0
    private val unpublishedPages: List<UnpublishedPage> = mutableListOf()

    override fun getPagesAsync(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<FilteredRepositoriesModel?>> = channelFlow {
        pageNumbers.minOrNull()?.let { pageNumOffset = it - 1 }
        loadedPages.set(0)
        pageNumbers.forEach { pageNumber ->
            launch {
                Log.d(LOG_TAG, "Fetching page #$pageNumber")
                val pageResource = datasource.browseRepositories(pageSize, pageNumber, filter)
                val currentlyLoaded = loadedPages.incrementAndGet()
                Log.d(LOG_TAG, "Currently loaded: $currentlyLoaded")
                if (pageNumber - (currentlyLoaded + pageNumOffset) <= 0) {
                    Log.d(LOG_TAG, "Emitting page #$pageNumber")
                    offer(pageResource)
                } else {
                    Log.d(LOG_TAG, "Saving unpublished page #$pageNumber for later")
                    pageResource.addToUnpublishedSafe(pageNumber)
                }
                processUnpublishedPages()
                if (loadedPages.get() == pageNumbers.size) {
                    Log.d(LOG_TAG, "All done. Closing channel")
                    (unpublishedPages as MutableList).clear()
                    close()
                }
            }
        }
    }

    @Synchronized
    private fun Resource<FilteredRepositoriesModel?>.addToUnpublishedSafe(pageNumber: Int) {
        (unpublishedPages as MutableList).let {
            it.add(pageNumber to this)
            it.sortBy { it.first }
        }
    }

    @Synchronized
    private fun ProducerScope<Resource<FilteredRepositoriesModel?>>.processUnpublishedPages() {
        val iterator = (unpublishedPages as MutableList).iterator()
        while (iterator.hasNext()) {
            val current = iterator.next()
            val currentlyLoaded = loadedPages.get()
            if (current.first - (currentlyLoaded + pageNumOffset) <= 0) {
                Log.d(LOG_TAG, "Emitting unpublished page #${current.first}")
                offer(current.second)
                iterator.remove()
            } else {
                break
            }
        }
    }
}