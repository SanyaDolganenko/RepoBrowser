package ua.dolhanenko.repobrowser.data.local.dao

import androidx.room.*
import ua.dolhanenko.repobrowser.data.local.entity.AppUser

@Dao
interface UsersCacheDao {
    @Query("SELECT * FROM appuser WHERE isActive = 1")
    fun getActiveUser(): AppUser?

    @Query("UPDATE appuser SET isActive=1 WHERE id LIKE :activeUserId")
    fun setUserAsActive(activeUserId: Long)

    @Query("UPDATE appuser SET isActive=0 WHERE id NOT LIKE :activeUserId")
    fun setOtherUsersAsInactive(activeUserId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: AppUser)

    @Delete
    fun delete(user: AppUser)

}