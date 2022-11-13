package com.example.learningconcepts.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningconcepts.Database.BookEntity
import com.example.learningconcepts.R
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context,val booklist:List<BookEntity>):RecyclerView.Adapter<FavouritesRecyclerAdapter.Favouriteholder>() {
 class Favouriteholder(view: View): RecyclerView.ViewHolder(view){
   val txtbookname:TextView=view.findViewById(R.id.tvbookname)
     val txtauthorname:TextView=view.findViewById(R.id.tvauthorname)
     val txtbookprice:TextView=view.findViewById(R.id.tvprice)
     val txtbookrating:TextView=view.findViewById(R.id.tvratings)
     val txtbookimage:ImageView=view.findViewById(R.id.ivbook)
     val rlcontent:RelativeLayout=view.findViewById(R.id.parentlayoutrecyclerviewitem)

 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favouriteholder {
   val view= LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_dashboard_singlerow,parent,false)
        return Favouriteholder(view)
    }

    override fun onBindViewHolder(holder: Favouriteholder, position: Int) {
      val book=booklist[position]

        holder.txtbookname.text=book.bookName
        holder.txtauthorname.text=book.bookAuthor
        holder.txtbookprice.text=book.bookPrice
        holder.txtbookrating.text=book.bookRating
         Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtbookimage)
    }

    override fun getItemCount(): Int {
     return booklist.size
    }
}