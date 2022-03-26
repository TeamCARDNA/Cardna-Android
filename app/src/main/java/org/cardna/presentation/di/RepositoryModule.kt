package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.datasource.InsightDataSource
import org.cardna.data.remote.datasource.UserDataSource
import org.cardna.data.repository.InsightRepositoryImpl
import org.cardna.data.repository.UserRepositoryImpl
import org.cardna.domain.repository.InsightRepository
import org.cardna.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    fun provideInsightRepository(insightDataSource: InsightDataSource): InsightRepository {
        return InsightRepositoryImpl(insightDataSource)
    }
}