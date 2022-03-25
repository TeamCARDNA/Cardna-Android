package org.cardna.domain.repository

import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData

interface UserRepository {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData
}