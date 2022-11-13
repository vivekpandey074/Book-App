package com.example.learningconcepts.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.learningconcepts.Database.BookDatabase
import com.example.learningconcepts.Database.BookEntity
import com.example.learningconcepts.R
import com.example.learningconcepts.utils.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject



class DescriptionActivity : AppCompatActivity() {
  lateinit var  textbookname:TextView
  lateinit var textbookauthorname:TextView
  lateinit var textbookPrice:TextView
  lateinit var textbookRating:TextView
  lateinit var imgbookimage:ImageView
  lateinit var textBookdesc:TextView
  lateinit var btnaddtofav:Button
  lateinit var progressBar: ProgressBar
  lateinit var  progresslayout:RelativeLayout
  lateinit var toolbar: androidx.appcompat.widget.Toolbar



  var bookId:String? ="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        textbookname=findViewById(R.id.tvbooknamedescription)
        textbookauthorname=findViewById(R.id.tvauthornamedescription)
        textbookPrice=findViewById(R.id.tvbookpricedescription)
        textbookRating=findViewById(R.id.tvratingsdescription)
        imgbookimage=findViewById(R.id.ivdescription)
        textBookdesc=findViewById(R.id.tvbookdescription)
        btnaddtofav=findViewById(R.id.btnaddtofavourite)
        progresslayout=findViewById(R.id.progressbarlayoutdescription)
        progressBar=findViewById(R.id.Progressbardescription)
        progresslayout.visibility= View.VISIBLE
        progressBar.visibility=View.VISIBLE
        toolbar=findViewById(R.id.descriptiontoolbar)

       setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


     if(intent!=null)
     {
         bookId=intent.getStringExtra("book_id")

     }else{
         finish()
         Toast.makeText(this,"Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
     }
        if(bookId=="100")
        {   finish()
            Toast.makeText(this,"Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
        }
        val queue= Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v1/book/get_book/"

        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)

        if(ConnectionManager().checkConnectivity(this))
        {
            val jsonRequest= object : JsonObjectRequest (
                Method.POST,url, jsonParams, Response.Listener{
                    try{

                        val success=it.getBoolean("success")
                        //if success is true
                        if(success)
                        {
                            progresslayout.visibility=View.GONE
                            val bookJsonobject=it.getJSONObject("book_data")   //first remove the progress bar


                          val bookimageurl=bookJsonobject.getString("image")
                            Picasso.get().load(bookJsonobject.getString("image")).error(R.drawable.default_book_cover).into(imgbookimage)
                            textbookname.text=bookJsonobject.getString("name")
                            textbookauthorname.text=bookJsonobject.getString("author")
                            textbookPrice.text=bookJsonobject.getString("price")

                            textbookRating.text=bookJsonobject.getString("rating")
                            textBookdesc.text=bookJsonobject.getString("description")


                             val bookdEntity=BookEntity(
                                 bookId?.toInt() as Int,
                                 textbookname.text.toString(),
                                 textbookauthorname.text.toString(),
                                 textbookPrice.text.toString(),
                                 textbookRating.text.toString(),
                                 textBookdesc.text.toString(),
                                 bookimageurl
                             )
                         val checkfav=DBAsyncTask(applicationContext,bookdEntity,1).execute() //check book is present in database or not
                            val isfav=checkfav.get()
                            if(isfav)
                            {
                                btnaddtofav.text="Remove from Favourites"
                                val favColo=ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                btnaddtofav.setBackgroundColor(favColo)
                            }else{
                                btnaddtofav.text=" Add to Favourites"
                                val nofavcolor=ContextCompat.getColor(applicationContext,R.color.black)
                                btnaddtofav.setBackgroundColor(nofavcolor)

                            }

                            btnaddtofav.setOnClickListener{
                                if(!DBAsyncTask(applicationContext,bookdEntity,1).execute().get()){
                                    val async=DBAsyncTask(applicationContext,bookdEntity,2).execute()
                                     val result=async.get()
                                    if(result){
                                        Toast.makeText(this,"Book added to favourites",Toast.LENGTH_SHORT).show()

                                        btnaddtofav.text="Remove From favourites"
                                        val favcolor=ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                        btnaddtofav.setBackgroundColor(favcolor)
                                    }else{
                                        Toast.makeText(this,"Some error occurred!",Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    val async=DBAsyncTask(applicationContext,bookdEntity,3).execute()
                                    val result=async.get()
                                    if(result){
                                        Toast.makeText(this,"Book removed from favourites",Toast.LENGTH_SHORT).show()

                                        btnaddtofav.text="Add to favourites"
                                        val nocolor=ContextCompat.getColor(applicationContext,R.color.black)
                                        btnaddtofav.setBackgroundColor(nocolor)
                                    }else{
                                        Toast.makeText(this,"Some error occurred!",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        }
                        else{
                            Toast.makeText(this,"Some error Occurred!!",Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {

                        Toast.makeText(this,"Some Error Occurred!",Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Log.d("MyTag","display:$it")
                    Toast.makeText(this,"Volley Error Occurred $it",Toast.LENGTH_SHORT).show()


                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="d9727ea0049c16"
                    return headers
                }
            }

            queue.add(jsonRequest)
        }else{
            val dialog= AlertDialog.Builder(this)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Not Found")
            dialog.setPositiveButton("OPEN SETTINGS"){text, listner->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
               finish()
            }
            dialog.setNegativeButton("CANCEL"){text, listner ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }


    }


   class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int): AsyncTask<Void, Void,Boolean>(){

       /*
        Mode 1-> check DB if the  book is favourites or not
        Mode 2-> Save the book into DS as favourites
        Mode 3 ->Remove the favourite book

        */
       val db= Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()   // Remember we need to initialize db globally so that whole async task can work on same db
       override fun doInBackground(vararg p0: Void?): Boolean {

          when(mode)
          {

              1->{
                  val book:BookEntity?=db.bookDao().getBookdById(bookEntity.book_id.toString())

                  db.close()  // it is necessary to close the database otherwise it will take unnecessary memory
                  return book!=null
              }

              2->{
                 db.bookDao().insertBook(bookEntity)
                  db.close()
                  return  true
              }

              3->{
                  db.bookDao().deleteBook(bookEntity)
                  db.close()
                  return true
              }
          }


           return false;
       }





   }




}