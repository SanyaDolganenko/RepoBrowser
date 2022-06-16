package ua.dolhanenko.repobrowser.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.dolhanenko.repobrowser.data.local.entity.AppUser
import ua.dolhanenko.repobrowser.data.local.entity.Repository

@Database(
    entities = [Repository::class, AppUser::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoriesCacheDao(): RepositoriesCacheDao
    abstract fun usersCacheDao(): UsersCacheDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
                    .also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "repo_browser_cache.db"
            ).fallbackToDestructiveMigration().build()
    }
}