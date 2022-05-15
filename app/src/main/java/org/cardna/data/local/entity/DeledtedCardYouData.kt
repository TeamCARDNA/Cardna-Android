package org.cardna.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_cardyou_table")
data class DeletedCardYouData(
    @PrimaryKey
    @ColumnInfo
    var cardId: Int,
)