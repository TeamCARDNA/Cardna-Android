package org.cardna.data.remote.api.card

import org.cardna.data.remote.model.card.ResponseMainCardData
import retrofit2.http.GET

interface CardService {
    @GET("card/main/{userId}")
    suspend fun getMainCard() : ResponseMainCardData
}