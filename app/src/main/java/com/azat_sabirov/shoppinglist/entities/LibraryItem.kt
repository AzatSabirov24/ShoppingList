package com.azat_sabirov.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "library")
data class LibraryItem (
    @PrimaryKey (autoGenerate = true)
    val Id: Int?,

    @ColumnInfo (name = "name")
    val name: String
)