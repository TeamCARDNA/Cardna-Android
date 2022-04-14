package org.cardna.data.remote.api.card

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import retrofit2.http.*

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

    // 친구의 카드나 조회
    @GET("card/you")
    suspend fun getCardYou(): ResponseCardYouData

    // 타인의 카드나 조회
    @GET("card/you/{userId}")
    suspend fun getOtherCardYou(
        @Path("userId")
        userId: Int?
    ): ResponseCardYouData

    // 카드나 작성, 카드너 작성 => 둘이 통합되면 CardMe 말고 Card로 통합
    @Multipart
    @POST("card")
    suspend fun postCreateCardMe(
        @PartMap body: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): ResponseCreateCardData





    @GET("card/main")
    suspend fun getMainCard(): ResponseMainCardData
}