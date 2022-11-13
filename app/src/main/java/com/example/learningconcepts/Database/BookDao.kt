package com.example.learningconcepts.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks():List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id=:bookId")   //use : before variable to tell compiler that variable coming from below function
    fun getBookdById(bookId:String) : BookEntity         //Notice all the function we created have only declaration not body and all operation we perform are take care of by room library

    

}