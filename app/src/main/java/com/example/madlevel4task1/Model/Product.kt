package com.example.madlevel4task1.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productTable")
data class Product(

    @ColumnInfo(name = "name")
    var productName: String,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)
