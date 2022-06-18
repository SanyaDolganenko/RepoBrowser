package ua.dolhanenko.repobrowser.data.repository

import android.util.Log
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import ua.dolhanenko.repobrowser.core.ILogger
import ua.dolhanenko.repobrowser.core.Resource
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

typealias UnpublishedPage = Pair<Int, Resource<IFilteredRepositoriesModel?>>

internal class ReposRepository @Inject constructor(
    private val pageSize: Int,
    private val githubDataSource: IGithubDataSource,
    private val reposCacheDataSource: IReposCacheDataSource,
    val logger: ILogger
) : IReposRepository {
    private companion object {
        const val LOG_TAG = "REPOS_REPO"
    }

    private val loadedPagesCount: AtomicInteger = AtomicInteger(0)
    private val sortedUnpublishedPages = mutableListOf<UnpublishedPage>()

    override fun downloadFilteredPages(
        filter: String,
        startPage: Int,
        endPage: Int
    ): Flow<Resource<IFilteredRepositoriesModel?>> = channelFlow {
        sortedUnpublishedPages.clear()
        loadedPagesCount.set(0)
        val pagesOffset = startPage - 1
        for (page in startPage..endPage) {
            launch {
                logger.d(LOG_TAG, "Fetching page #$page")
                val pageResource = githubDataSource.browseRepositories(pageSize, page, filter)
                val loadedPages = loadedPagesCount.incrementAndGet()
                pageResource.addToUnpublishedSafe(page)
                this@channelFlow.processUnpublishedPages(loadedPages, pagesOffset)
            }
        }
    }

    @Synchronized
    private fun Resource<IFilteredRepositoriesModel?>.addToUnpublishedSafe(pageNumber: Int) {
        sortedUnpublishedPages.let {
            it.add(pageNumber to this)
            it.sortBy { it.first }
        }
    }

    @Synchronized
    private suspend fun ProducerScope<Resource<IFilteredRepositoriesModel?>>.processUnpublishedPages(
        loadedPagesCount: Int,
        pagesOffset: Int
    ) {
        logger.d(
            LOG_TAG,
            "Processing unpublished pages. Loaded count: $loadedPagesCount offset: $pagesOffset"
        )
        val iterator = sortedUnpublishedPages.iterator()
        while (iterator.hasNext()) {
            val current = iterator.next()
            val page = current.second
            val pageNumber = current.first
            if (pageNumber <= loadedPagesCount + pagesOffset) {
                Log.d(LOG_TAG, "Emitting unpublished page #$pageNumber")
                send(page)
                iterator.remove()
            } else {
                break
            }
        }
        if (sortedUnpublishedPages.isEmpty()) {
//            channel.close()
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