package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.api.insight.InsightService
import org.cardna.data.remote.api.user.UserService
import org.cardna.data.remote.datasource.InsightDataSource
import org.cardna.data.remote.datasource.InsightDataSourceImpl
import org.cardna.data.remote.datasource.UserDataSource
import org.cardna.data.remote.datasource.UserDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideUserDataSource(userService: UserService): UserDataSource {
        return UserDataSourceImpl(userService)
    }

    @Provides
    @Singleton
    fun provideInsightDataSource(insightService: InsightService): InsightDataSource {
        return InsightDataSourceImpl(insightService)
    }
}