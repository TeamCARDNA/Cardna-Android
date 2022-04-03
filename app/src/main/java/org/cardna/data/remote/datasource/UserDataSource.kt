package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.RequestPostReportUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData
import org.cardna.data.remote.model.user.ResponsePostReportUserData
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataSource {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData

    suspend fun postReportUser(body: RequestPostReportUserData): ResponsePostReportUserData
}