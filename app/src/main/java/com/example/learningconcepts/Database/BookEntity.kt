package com.example.learningconcepts.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="books")
data class BookEntity(
    @PrimaryKey   val book_id:Int,      //these values will be column inside our table class
    @ColumnInfo(name="book_name")val bookName:String,             //name we have written here are following convention of naming variable
   @ColumnInfo(name="book_author") val bookAuthor: String,           //And we have to make coloumn name according to its convention
    @ColumnInfo(name="book_price") val bookPrice:String,
    @ColumnInfo(name="book_rating") val bookRating:String,
    @ColumnInfo(name="book_desc") val bookDesc: String,
    @ColumnInfo(name="book_image") val bookImage:String
)
