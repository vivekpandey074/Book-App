package com.example.learningconcepts.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.learningconcepts.Adapter.DashboardRecyclerAdapter
import com.example.learningconcepts.Model.Book
import com.example.learningconcepts.R
import com.example.learningconcepts.utils.ConnectionManager
import org.json.JSONException
import java.util.*
import javax.xml.transform.OutputKeys.METHOD
import kotlin.Comparator
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Dashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class Dashboard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var rvdashboard: RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progresslayout:RelativeLayout
    lateinit var progressbar:ProgressBar
    var bookInfoList= arrayListOf<Book>()
    var ratingComparator= Comparator<Book> {book1, book2->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }

    }

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
        val view= inflater.inflate(R.layout.fragment_dashboard, container, false)
        progresslayout=view.findViewById(R.id.progressbarlayout)
     progresslayout.visibility=View.VISIBLE
       setHasOptionsMenu(true)   //this method is used only used in fragments to add menu // in activity it is done automatically by compiler

      rvdashboard=view.findViewById(R.id.rvdashboard) //using view here before findviewby id  because this view will be display in container available for fragment
     layoutmanager=LinearLayoutManager(activity)







       val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context))
        {

           try{
               progresslayout.visibility=View.GONE
               val jsonObjectRequest= object: JsonObjectRequest(Method.GET,url,null,
                   Response.Listener {
                       val success= it.getBoolean("success")
                       if(success)
                       {
                           val data= it.getJSONArray("data")
                           for(i in 0 until data.length())
                           {
                               val bookJsonObject=data.getJSONObject(i)
                               val bookobject= Book(
                                   bookJsonObject.getString("book_id"),
                                   bookJsonObject.getString("name"),
                                   bookJsonObject.getString("author"),
                                   bookJsonObject.getString("rating"),
                                   bookJsonObject.getString("price"),
                                   bookJsonObject.getString("image"),

                                   )
                               bookInfoList.add(bookobject)
                               recyclerAdapter= DashboardRecyclerAdapter(activity as Context, bookInfoList)
                               rvdashboard.adapter=recyclerAdapter
                               rvdashboard.layoutManager=layoutmanager

                               rvdashboard.addItemDecoration(
                                   DividerItemDecoration(
                                       rvdashboard.context,
                                       (layoutmanager as LinearLayoutManager).orientation
                                   )
                               )
                           }
                       }
                       else{
                           Toast.makeText(activity as Context,"Some Error as Occurred!!!",Toast.LENGTH_SHORT).show()
                       }


                   },Response.ErrorListener {

                    if(activity!=null)
                    {
                        Toast.makeText(activity as Context,"Volley Error!!",Toast.LENGTH_SHORT).show()
                    }
                   })
               {
                   override fun getHeaders(): MutableMap<String, String> {
                       val headers=HashMap<String, String>()
                       headers["Content-type"]="application/json"
                       headers["token"]="d9727ea0049c16"
                       return headers
                   }
               }
               queue.add(jsonObjectRequest)

           }
            catch(e: JSONException)
            {
                Toast.makeText(activity as Context,"Some unexpected error occurred!!",Toast.LENGTH_SHORT).show()
            }

        }else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Not Found")
            dialog.setPositiveButton("OPEN SETTINGS"){text, listner->
              val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("CANCEL"){text, listner ->
              ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {  //used to create menu option on toolbar
      inflater.inflate(R.menu.menu_dashboard,menu)    //inflater is used to inflate the xml file and meny item is just variable for holding our meny items
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
       recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    }
