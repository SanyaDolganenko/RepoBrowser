package ua.dolhanenko.repobrowser.domain.model

import ua.dolhanenko.repobrowser.data.local.entity.AppUser
import ua.dolhanenko.repobrowser.data.remote.entity.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.entity.Owner
import ua.dolhanenko.repobrowser.data.remote.entity.RepoResponse
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse


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

fun UserResponse.toModel(): UserModel = UserModel(id, login)

fun UserModel.toDbEntity(isActive: Boolean): AppUser = AppUser(id, userName, isActive)