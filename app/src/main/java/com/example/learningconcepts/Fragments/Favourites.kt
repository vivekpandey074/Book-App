package com.example.learningconcepts.Fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.room.Room
import com.example.learningconcepts.Adapter.FavouritesRecyclerAdapter
import com.example.learningconcepts.Database.BookDatabase
import com.example.learningconcepts.Database.BookEntity
import com.example.learningconcepts.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Favourites.newInstance] factory method to
 * create an instance of this fragment.
 */
class Favourites : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var rvfavourite:RecyclerView
    lateinit var progressbarFavourites: ProgressBar
    lateinit var progressbarlayoutfavourites:RelativeLayout
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var rvadapter:FavouritesRecyclerAdapter
   var dbbooklist= listOf<BookEntity>()







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view=inflater.inflate(R.layout.fragment_favourites, container, false)
        rvfavourite=view.findViewById(R.id.rvfavourites)
        progressbarFavourites=view.findViewById(R.id.ProgressbarFavourites)
        progressbarlayoutfavourites=view.findViewById(R.id.progressbarlayoutfavourites)
       layoutManager=LinearLayoutManager(activity as Context)
        dbbooklist=RetrieveFavourites(activity as Context).execute().get()

        if(dbbooklist!=null && activity!=null)
        {
            progressbarlayoutfavourites.visibility=View.GONE
            rvadapter= FavouritesRecyclerAdapter(activity as Context,dbbooklist)
            rvfavourite.adapter=rvadapter
            rvfavourite.layoutManager=layoutManager
        }



        return view
    }
class RetrieveFavourites(val context : Context): AsyncTask<Void, Void,List<BookEntity>>(){
    override fun doInBackground(vararg p0: Void?): List<BookEntity> {
          val db= Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()

        return db.bookDao().getAllBooks()
    }

}

}