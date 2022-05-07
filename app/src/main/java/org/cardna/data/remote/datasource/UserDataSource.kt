package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.user.*
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataSource {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData

    suspend fun postReportUser(body: RequestPostReportUserData): ResponsePostReportUserData

    suspend fun getUser(): ResponseUserData
}