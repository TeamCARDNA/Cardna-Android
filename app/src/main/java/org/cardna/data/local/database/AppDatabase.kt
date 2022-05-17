package org.cardna.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.cardna.data.local.dao.DeletedCardYouDao
import org.cardna.data.local.entity.DeletedCardYouData

@Database(entities = [DeletedCardYouData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val deletedCardYouDao: DeletedCardYouDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "deleted cardyou record database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
