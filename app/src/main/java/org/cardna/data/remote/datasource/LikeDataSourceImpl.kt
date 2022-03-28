package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.like.LikeService
import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData
import javax.inject.Inject

class LikeDataSourceImpl @Inject constructor(
    private val likeService: LikeService
) : LikeDataSource {

    override suspend fun postLike(body: RequestLikeData): ResponseLikeData {
        return likeService.postLike(body)
    }
}