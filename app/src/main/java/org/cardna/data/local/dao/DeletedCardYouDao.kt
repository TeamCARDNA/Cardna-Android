package org.cardna.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.cardna.data.local.entity.DeletedCardYouData

@Dao
interface DeletedCardYouDao {
    @Query("SELECT * FROM deleted_cardyou_table")
    suspend fun getAllDeletedCardYou(): MutableList<DeletedCardYouData>?

    @Insert
    suspend fun insertDeletedCardYou(deletedCardYouData: DeletedCardYouData)
}