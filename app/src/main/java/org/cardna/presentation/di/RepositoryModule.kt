package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.datasource.*
import org.cardna.data.repository.*
import org.cardna.domain.repository.*
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
    fun provideCardRepository(cardDataSource: CardDataSource): CardRepository {
        return CardRepositoryImpl(cardDataSource)
    }

    @Provides
    @Singleton
    fun provideLikeRepository(likeDataSource: LikeDataSource): LikeRepository {
        return LikeRepositoryImpl(likeDataSource)
    }

    @Provides
    @Singleton
    fun provideMyPageRepository(myPageDataSource: MyPageDataSource): MyPageRepository {
        return MyPageRepositoryImpl(myPageDataSource)
    }
}