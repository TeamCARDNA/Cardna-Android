package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.user.UserService
import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserDataSource {

    override suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData {
        return userService.deleteUser(body)
    }
}