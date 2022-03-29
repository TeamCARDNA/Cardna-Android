package org.cardna.data.remote.api.card

import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
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
}