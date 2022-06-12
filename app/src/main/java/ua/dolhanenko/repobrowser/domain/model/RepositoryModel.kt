package ua.dolhanenko.repobrowser.domain.model

import java.util.*


data class RepositoryModel(
    val id: String, val title: String, val description: String,
    val stars: Long, val watchers: Long, val language: String?,
    val url: String, val owner: OwnerModel
) {
    var readAt: Date? = null
}

data class OwnerModel(val name: String, val avatarUrl: String)