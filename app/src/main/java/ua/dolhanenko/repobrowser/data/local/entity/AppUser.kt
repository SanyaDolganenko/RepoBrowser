package ua.dolhanenko.repobrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class AppUser(
    @PrimaryKey val id: Long, val userName: String, val isActive: Boolean = false,
    val lastUsedToken: String
)