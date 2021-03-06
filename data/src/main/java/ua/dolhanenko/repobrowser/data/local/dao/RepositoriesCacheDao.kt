package ua.dolhanenko.repobrowser.data.local.dao

import androidx.room.*
import ua.dolhanenko.repobrowser.data.local.entity.Repository

@Dao
internal interface RepositoriesCacheDao {
    @Query("SELECT * FROM repository WHERE byUserId LIKE :byUserId ORDER BY readTime DESC")
    fun getItems(byUserId: Long): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repositories: List<Repository>)

    @Delete
    fun delete(repository: Repository)

    @Query("DELETE FROM repository")
    fun deleteAll()
}