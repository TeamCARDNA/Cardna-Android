package org.cardna.data.repository

import org.cardna.data.remote.datasource.LikeDataSource
import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData
import org.cardna.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeDataSource: LikeDataSource
) : LikeRepository {

    override suspend fun postLike(body: RequestLikeData): ResponseLikeData {
        return likeDataSource.postLike(body)
    }
}