package org.cardna.data.repository

import org.cardna.data.remote.datasource.UserDataSource
import org.cardna.data.remote.model.user.*
import org.cardna.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {

    override suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData {
        return userDataSource.deleteUser(body)
    }

    override suspend fun postReportUser(body: RequestPostReportUserData): ResponsePostReportUserData {
        return userDataSource.postReportUser(body)
    }

    override suspend fun getUser(): ResponseUserData {
        return userDataSource.getUser()
    }
}