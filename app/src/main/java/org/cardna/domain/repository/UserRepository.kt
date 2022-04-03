package org.cardna.domain.repository

import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.RequestPostReportUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData
import org.cardna.data.remote.model.user.ResponsePostReportUserData

interface UserRepository {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData

    suspend fun postReportUser(body: RequestPostReportUserData): ResponsePostReportUserData
}