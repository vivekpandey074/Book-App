package com.example.learningconcepts.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.learningconcepts.Activity.DescriptionActivity
import com.example.learningconcepts.Fragments.Dashboard
import com.example.learningconcepts.Model.Book
import com.example.learningconcepts.R
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemlist:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DasboardViewholder>() {
    class DasboardViewholder(view: View): RecyclerView.ViewHolder(view)
    {
        val tvbookname:TextView=view.findViewById(R.id.tvbookname)
        val tvauthorname:TextView=view.findViewById(R.id.tvauthorname)
        val tvprice:TextView=view.findViewById(R.id.tvprice)
        val tvratings:TextView=view.findViewById(R.id.tvratings)
        val ivbook:ImageView=view.findViewById(R.id.ivbook)
        val parentlayoutcontent:RelativeLayout=view.findViewById(R.id.parentlayoutrecyclerviewitem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DasboardViewholder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_dashboard_singlerow,parent,false)
        return DasboardViewholder(view)
    }

    override fun onBindViewHolder(holder: DasboardViewholder, position: Int) {
        val x = itemlist[position]
        holder.tvbookname.text = x.bookName
        holder.tvauthorname.text = x.bookAuthor
        holder.tvprice.text = x.bookCost
        holder.tvratings.text = x.bookRating
       // holder.ivbook.setImageResource(x.bookImage)
        Picasso.get().load(x.bookImage).error(R.drawable.default_book_cover).into(holder.ivbook)
        holder.parentlayoutcontent.setOnClickListener {
           val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",x.bookId)
           context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
       return itemlist.size
    }
}