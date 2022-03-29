package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.datasource.CardDataSource
import org.cardna.data.remote.datasource.InsightDataSource
import org.cardna.data.remote.datasource.LikeDataSource
import org.cardna.data.remote.datasource.UserDataSource
import org.cardna.data.repository.CardRepositoryImpl
import org.cardna.data.repository.InsightRepositoryImpl
import org.cardna.data.repository.LikeRepositoryImpl
import org.cardna.data.repository.UserRepositoryImpl
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.InsightRepository
import org.cardna.domain.repository.LikeRepository
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

    @Provides
    @Singleton
    fun CardRepository(cardDataSource: CardDataSource): CardRepository {
        return CardRepositoryImpl(cardDataSource)
    }

    @Provides
    @Singleton
    fun LikeRepository(likeDataSource: LikeDataSource): LikeRepository {
        return LikeRepositoryImpl(likeDataSource)
    }
}