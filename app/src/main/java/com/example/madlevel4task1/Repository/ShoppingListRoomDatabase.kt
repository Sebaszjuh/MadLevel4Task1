package com.example.madlevel4task1.Repository

import androidx.room.RoomDatabase
import com.example.madlevel4task1.DAO.ProductDao

abstract class ShoppingListRoomDatabase : RoomDatabase(){

    abstract fun productDao(): ProductDao


}