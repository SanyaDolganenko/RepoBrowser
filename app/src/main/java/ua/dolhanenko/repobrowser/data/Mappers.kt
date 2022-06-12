package ua.dolhanenko.repobrowser.domain.model

import ua.dolhanenko.repobrowser.data.local.entity.AppUser
import ua.dolhanenko.repobrowser.data.local.entity.Repository
import ua.dolhanenko.repobrowser.data.remote.entity.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.entity.Owner
import ua.dolhanenko.repobrowser.data.remote.entity.RepoResponse
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse
import java.util.*


fun RepoResponse.toModel(): RepositoryModel {
    return RepositoryModel(
        id,
        name,
        description ?: "",
        stargazers_count ?: 0,
        watchers_count ?: 0, language, html_url ?: "",
        owner?.toModel() ?: OwnerModel("", "")
    )
}

fun FilteredReposResponse.toModel(pageNumber: Int): FilteredRepositoriesModel {
    return FilteredRepositoriesModel(pageNumber, total_count, items.map { it.toModel() })
}

fun Owner.toModel(): OwnerModel = OwnerModel(login, avatar_url)

fun UserResponse.toModel(userToken: String): UserModel = UserModel(id, login, userToken)

fun UserModel.toDbEntity(isActive: Boolean): AppUser =
    AppUser(id, userName, isActive, lastUsedToken)

fun AppUser.toModel(): UserModel = UserModel(id, userName, lastUsedToken)

fun RepositoryModel.toDbEntity(userId: Long, viewedAt: Long? = null): Repository {
    return Repository(
        id, viewedAt, userId, title, description, stars, watchers,
        language, url, owner.name, owner.avatarUrl
    )
}

fun Repository.toModel(): RepositoryModel {
    return RepositoryModel(
        id, title, description, stars, watchers,
        language, url, OwnerModel(ownerName, ownerLogoUrl)
    ).also { model ->
        this.readAt?.let {
            model.readAt = Date(it)
        }
    }
}