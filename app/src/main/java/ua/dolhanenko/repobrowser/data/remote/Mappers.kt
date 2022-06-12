package ua.dolhanenko.repobrowser.data.remote

import ua.dolhanenko.repobrowser.data.remote.entity.FilteredReposResponse
import ua.dolhanenko.repobrowser.data.remote.entity.Owner
import ua.dolhanenko.repobrowser.data.remote.entity.RepoResponse
import ua.dolhanenko.repobrowser.data.remote.entity.UserResponse
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.OwnerModel
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.UserModel


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