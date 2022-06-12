package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.local.entity.AppUser
import ua.dolhanenko.repobrowser.data.local.entity.Repository
import ua.dolhanenko.repobrowser.domain.model.OwnerModel
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.domain.model.UserModel
import java.util.*


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