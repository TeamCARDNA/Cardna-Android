package org.cardna.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cardna.data.remote.api.alarm.AlarmService
import org.cardna.data.remote.api.auth.AuthService
import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.api.friend.FriendService
import org.cardna.data.remote.api.insight.InsightService
import org.cardna.data.remote.api.like.LikeService
import org.cardna.data.remote.api.mypage.MyPageService
import org.cardna.data.remote.api.user.UserService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAlarmService(@RetrofitModule.GsonConverter retrofit: Retrofit): AlarmService {
        return retrofit.create(AlarmService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(@RetrofitModule.GsonConverter retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideCardService(@RetrofitModule.GsonConverter retrofit: Retrofit): CardService {
        return retrofit.create(CardService::class.java)
    }

    @Provides
    @Singleton
    fun provideFriendService(@RetrofitModule.GsonConverter retrofit: Retrofit): FriendService {
        return retrofit.create(FriendService::class.java)
    }

    @Provides
    @Singleton
    fun provideInsightService(@RetrofitModule.GsonConverter retrofit: Retrofit): InsightService {
        return retrofit.create(InsightService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikeService(@RetrofitModule.GsonConverter retrofit: Retrofit): LikeService {
        return retrofit.create(LikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageService(@RetrofitModule.MoshiConverter retrofit: Retrofit): MyPageService {
        return retrofit.create(MyPageService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@RetrofitModule.GsonConverter retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}