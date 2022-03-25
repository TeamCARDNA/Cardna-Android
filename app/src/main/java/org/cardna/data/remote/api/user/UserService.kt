package org.cardna.data.remote.api.user

import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData
import retrofit2.http.Body
import retrofit2.http.DELETE

interface UserService {

    @DELETE("user")
    suspend fun deleteUser(
        @Body body: RequestDeleteUserData
    ): ResponseDeleteUserData
}