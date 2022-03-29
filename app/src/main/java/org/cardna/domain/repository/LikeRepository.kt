package org.cardna.domain.repository

import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData

interface LikeRepository {

    suspend fun postLike(body: RequestLikeData): ResponseLikeData
}