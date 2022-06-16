package ua.dolhanenko.repobrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.dolhanenko.repobrowser.domain.model.IUserModel

@Entity
internal class AppUser(
    @PrimaryKey override val id: Long,
    override val userName: String,
    val isActive: Boolean = false,
    override var lastUsedToken: String?
) : IUserModel