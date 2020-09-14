package com.example.madlevel4task1.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.madlevel4task1.DAO.ProductDao
import com.example.madlevel4task1.Model.Product


// Database van product class
@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ShoppingListRoomDatabase : RoomDatabase(){

    // instantieren van DAO
    abstract fun productDao(): ProductDao


    // Companion is een soort 'vervanger'  voor static
    companion object {
        private const val DATABASE_NAME = "SHOPPING_LIST_DATABASE"

        // Volatile makes it writeable for other instances
        @Volatile
        private var shoppingListRoomDatabaseInstance: ShoppingListRoomDatabase? = null


        // Builder method om de database te bouwen
        fun getDatabase(context: Context): ShoppingListRoomDatabase? {
            if (shoppingListRoomDatabaseInstance == null) {
                synchronized(ShoppingListRoomDatabase::class.java) {
                    if (shoppingListRoomDatabaseInstance == null) {
                        shoppingListRoomDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,ShoppingListRoomDatabase::class.java, DATABASE_NAME).build()
                    }
                }
            }
            return shoppingListRoomDatabaseInstance
        }
    }

}
