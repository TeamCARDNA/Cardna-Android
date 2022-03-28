package org.cardna.data.remote.api.like

import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData
import retrofit2.http.Body
import retrofit2.http.POST

interface LikeService {

    @POST("like")
    suspend fun postLike(
        @Body body: RequestLikeData
    ): ResponseLikeData
}