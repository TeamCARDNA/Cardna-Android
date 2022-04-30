package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.api.auth.AuthService
import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.api.friend.FriendService
import org.cardna.data.remote.api.insight.InsightService
import org.cardna.data.remote.api.like.LikeService
import org.cardna.data.remote.api.mypage.MyPageService
import org.cardna.data.remote.api.user.UserService
import org.cardna.data.remote.datasource.*
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

    @Provides
    @Singleton
    fun provideCardDataSource(cardService: CardService): CardDataSource {
        return CardDataSourceImpl(cardService)
    }

    @Provides
    @Singleton
    fun provideLikeDataSource(likeService: LikeService): LikeDataSource {
        return LikeDataSourceImpl(likeService)
    }

    @Provides
    @Singleton
    fun provideMyPageDataSource(myPageService: MyPageService): MyPageDataSource {
        return MyPageDataSourceImpl(myPageService)
    }

    @Provides
    @Singleton
    fun provideFriendDataSource(friendService: FriendService): FriendDataSource {
        return FriendDataSourceImpl(friendService)
    }

    @Provides
    @Singleton
    fun providerAuthDataSource(authService: AuthService): AuthDataSource {
        return AuthDataSourceImpl(authService)
    }
}