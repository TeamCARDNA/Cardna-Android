package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData

interface LikeDataSource {

    suspend fun postLike(body: RequestLikeData): ResponseLikeData
}