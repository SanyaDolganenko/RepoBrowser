package ua.dolhanenko.repobrowser.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import ua.dolhanenko.repobrowser.domain.model.IOwnerModel
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import java.util.*

@Entity(primaryKeys = ["id", "byUserId"])
data class Repository(
    override val id: String,
    val byUserId: Long,
    override val title: String,
    override val description: String,
    override val stars: Long,
    override val watchers: Long,
    override val language: String?,
    override val url: String?,
    val ownerName: String?,
    val ownerLogoUrl: String?,
    val readTime: Long?
) : IRepositoryModel {
    override val owner: IOwnerModel?
        get() {
            return if (ownerName != null && ownerLogoUrl != null) {
                object : IOwnerModel {
                    override val name: String = ownerName
                    override val avatarUrl: String = ownerLogoUrl
                }
            } else {
                null
            }
        }

    @Ignore
    override var readAt: Date? = readTime?.let { Date(it) }

    override fun clone(): IRepositoryModel {
        return this.copy()
    }
}