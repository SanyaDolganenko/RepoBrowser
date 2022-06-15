package ua.dolhanenko.repobrowser.domain.model

import java.util.*


interface IRepositoryModel {
    val id: String
    val title: String
    val description: String
    val stars: Long
    val watchers: Long
    val language: String?
    val url: String?
    val owner: IOwnerModel?
    var readAt: Date?

    fun clone(): IRepositoryModel
}

interface IOwnerModel {
    val name: String
    val avatarUrl: String
}