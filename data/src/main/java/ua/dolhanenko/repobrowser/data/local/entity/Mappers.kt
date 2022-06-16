package ua.dolhanenko.repobrowser.data.local.entity

import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.domain.model.IUserModel


internal fun IUserModel.toDbEntity(isActive: Boolean): AppUser =
    AppUser(id, userName, isActive, lastUsedToken)

internal fun IRepositoryModel.toDbEntity(userId: Long, viewedAt: Long? = null): Repository {
    return Repository(
        id, userId, title, description, stars, watchers,
        language, url, owner?.name, owner?.avatarUrl, viewedAt
    )
}