package com.example.learningconcepts.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[BookEntity::class], version=1)
abstract class BookDatabase: RoomDatabase() {

  abstract  fun bookDao():BookDao   //this function will serve as doorway for dao operations
                                   //this will return dao interface

}