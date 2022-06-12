package ua.dolhanenko.repobrowser.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "byUserId"])
class Repository(
    val id: String,
    val readAt: Long?,
    val byUserId: Long,
    val title: String,
    val description: String,
    val stars: Long,
    val watchers: Long,
    val language: String?,
    val url: String,
    val ownerName: String,
    val ownerLogoUrl: String
)