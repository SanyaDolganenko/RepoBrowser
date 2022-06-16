package ua.dolhanenko.repobrowser.data.remote.entity

import ua.dolhanenko.repobrowser.domain.model.IFilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.IOwnerModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import java.util.*


internal data class FilteredReposResponse(
    val total_count: Long,
    override val items: List<RepoResponse>
) : IFilteredRepositoriesModel {
    override var pageNumber: Int = 0
    override val foundInTotal: Long
        get() = total_count
}

internal data class RepoResponse(
    override val id: String,
    val name: String,
    val full_name: String?,
    val stargazers_count: Long,
    val watchers_count: Long,
    val html_url: String?,
    override val description: String,
    override val language: String?,
    override val owner: Owner?
) : IRepositoryModel {
    override val title: String get() = name
    override val stars: Long get() = stargazers_count
    override val watchers: Long get() = watchers_count
    override val url: String? get() = html_url
    override var readAt: Date? = null

    override fun clone(): IRepositoryModel {
        return this.copy()
    }
}

internal data class Owner(val login: String, val avatar_url: String) : IOwnerModel {
    override val name: String get() = login
    override val avatarUrl: String get() = avatar_url
}