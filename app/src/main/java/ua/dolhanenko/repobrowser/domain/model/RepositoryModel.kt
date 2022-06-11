package ua.dolhanenko.repobrowser.domain.model


data class RepositoryModel(
    val id: String, val title: String, val description: String,
    val stars: Long, val watchers: Long, val language: String?,
    val url: String, val owner: OwnerModel
) {
    var isRead: Boolean = false
}

data class OwnerModel(val name: String, val avatarUrl: String)