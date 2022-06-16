package ua.dolhanenko.repobrowser.data.repository

import android.util.Log
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

typealias UnpublishedPage = Pair<Int, Resource<IFilteredRepositoriesModel?>>

class ReposRepository @Inject constructor(
    private val pageSize: Int,
    private val githubDataSource: IGithubDataSource,
    private val reposCacheDataSource: IReposCacheDataSource
) : IReposRepository {
    private companion object {
        const val LOG_TAG = "REPOS_REPO"
    }

    private val loadedPages: AtomicInteger = AtomicInteger(0)
    private var pageNumOffset: Int = 0
    private val unpublishedPages: List<UnpublishedPage> = mutableListOf()

    override fun getFreshFilteredPagesAsync(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<IFilteredRepositoriesModel?>> = channelFlow {
        pageNumbers.minOrNull()?.let { pageNumOffset = it - 1 }
        loadedPages.set(0)
        pageNumbers.forEach { pageNumber ->
            launch {
                Log.d(LOG_TAG, "Fetching page #$pageNumber")
                val pageResource = githubDataSource.browseRepositories(pageSize, pageNumber, filter)
                val currentlyLoaded = loadedPages.incrementAndGet()
                Log.d(LOG_TAG, "Currently loaded: $currentlyLoaded")
                if (pageNumber - (currentlyLoaded + pageNumOffset) <= 0) {
                    Log.d(LOG_TAG, "Emitting page #$pageNumber")
                    offer(pageResource)
                } else {
                    Log.d(
                        LOG_TAG,
                        "Saving unpublished page #$pageNumber for later"
                    )
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
    private fun Resource<IFilteredRepositoriesModel?>.addToUnpublishedSafe(pageNumber: Int) {
        (unpublishedPages as MutableList).let {
            it.add(pageNumber to this)
            it.sortBy { it.first }
        }
    }

    @Synchronized
    private fun ProducerScope<Resource<IFilteredRepositoriesModel?>>.processUnpublishedPages() {
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

    override fun getReadItems(byUserId: Long): List<IRepositoryModel> {
        return reposCacheDataSource.getItems(byUserId)
    }

    override fun insertRead(repository: IRepositoryModel, forUserId: Long, viewedAt: Long) {
        reposCacheDataSource.insert(repository, forUserId, viewedAt)
    }

    override fun deleteRead(repository: IRepositoryModel) {
        reposCacheDataSource.delete(repository)
    }
}