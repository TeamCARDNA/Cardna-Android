package org.cardna.data.remote.api.card

import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CardService {

    @GET("card/info/{cardId}")
    suspend fun getDetailCard(
        @Path("cardId") cardId: Int
    ): ResponseDetailCardData

    @DELETE("card/{cardId}")
    suspend fun deleteCard(
        @Path("cardId") cardId: Int
    ): ResponseDeleteCardData

    @PUT("card/box/{cardId}")
    suspend fun putKeepOrAddCard(
        @Path("cardId") cardId: Int
    ): ResponseKeepOrAddCardData


    // 나의 카드나 조회
    @GET("card/me")
    suspend fun getCardMe(): ResponseCardMeData

    // 타인의 카드나 조회
    @GET("card/me/{userId}")
    suspend fun getOtherCardMe(
        @Path("userId")
        userId: Int?
    ): ResponseCardMeData

    // 나의 카드너 전체 조회
    @GET("card/you")
    suspend fun getCardYou(): ResponseCardYouData

    // 카드너 전체 조회
    @GET("card/you/{userId}")
    suspend fun getOtherCardYou(
        @Path("userId")
        userId: Int?
    ): ResponseCardYouData

    @GET("card/main")
    suspend fun getMainCard(): ResponseMainCardData
}