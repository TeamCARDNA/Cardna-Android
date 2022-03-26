package org.cardna.data.remote.api.card

import org.cardna.data.remote.model.card.ResponseDetailCardData
import retrofit2.http.GET
import retrofit2.http.Path

interface CardService {

    @GET("card/info/{cardId}")
    suspend fun getDetailCard(
        @Path("cardId") cardId: Int
    ): ResponseDetailCardData
}