package org.cardna.domain.repository

import org.cardna.data.remote.model.user.*

interface UserRepository {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData

    suspend fun postReportUser(body: RequestPostReportUserData): ResponsePostReportUserData

    suspend fun getUser(): ResponseUserData
}