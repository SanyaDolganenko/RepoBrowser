package ua.dolhanenko.repobrowser.data.remote.entity

import ua.dolhanenko.repobrowser.domain.model.IUserModel


data class UserResponse(override val id: Long, val login: String) : IUserModel {
    override val userName: String get() = login
    override var lastUsedToken: String? = null
}