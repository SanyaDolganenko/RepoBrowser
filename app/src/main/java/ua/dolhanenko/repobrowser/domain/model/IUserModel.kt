package ua.dolhanenko.repobrowser.domain.model


interface IUserModel {
    val id: Long
    val userName: String
    var lastUsedToken: String?
}