package org.cardna.presentation.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.cardna.data.local.dao.DeletedCardYouDao
import org.cardna.data.local.database.AppDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "deleted cardyou record database"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDeletedCardYouDao(database: AppDatabase): DeletedCardYouDao =
        database.deletedCardYouDao
}
