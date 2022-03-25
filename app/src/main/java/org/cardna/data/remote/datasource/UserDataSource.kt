package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData

interface UserDataSource {

    suspend fun deleteUser(body: RequestDeleteUserData): ResponseDeleteUserData
}