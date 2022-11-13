package com.example.learningconcepts.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.learningconcepts.*
import com.example.learningconcepts.Fragments.About
import com.example.learningconcepts.Fragments.Dashboard
import com.example.learningconcepts.Fragments.Favourites
import com.example.learningconcepts.Fragments.Profile
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
  lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
  lateinit var navigationView: NavigationView
  lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
  lateinit var coordinatorLayout: CoordinatorLayout
  lateinit var frameLayout: FrameLayout
 var previousMenuItem:MenuItem? =null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      drawerLayout=findViewById(R.id.drawerLayout)
      toolbar=findViewById(R.id.toolbar)
     navigationView=findViewById(R.id.navigationView)
      setuptoolbar()

 opendashboard()



        actionBarDrawerToggle=ActionBarDrawerToggle(this,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        ) //This class provides a handy way to tie together the functionality of DrawerLayout and the framework ActionBar to implement the recommended design for navigation drawers.

        drawerLayout.addDrawerListener(actionBarDrawerToggle) // this will put clicks listners  and it 's clicks are not working here
        actionBarDrawerToggle.syncState()  //after putting this line hamburger icon autmatically come, it creates sync between drawer and action bar
        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false

            }

            it.isChecked=true
            it.isCheckable=true
            previousMenuItem=it


             when(it.itemId)
             {
                 R.id.dashboard -> {
                           opendashboard()
                           drawerLayout.closeDrawers()
                 }

                 R.id.favourite -> {
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.framelayout, Favourites())
                         .commit()
                     supportActionBar?.title="Favourites"
                     drawerLayout.closeDrawers()
                 }
                 R.id.profile -> {
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.framelayout, Profile())
                         .commit()
                     supportActionBar?.title="Profile"
                     drawerLayout.closeDrawers()
                 }
                 R.id.about -> {
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.framelayout, About())
                         .commit()
                     supportActionBar?.title="About"
                     drawerLayout.closeDrawers()
                 }


             }


            return@setNavigationItemSelectedListener true
        }


    }

    fun setuptoolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //menu button will be displayed through this
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   //the home button is also know as menu item
        val id=item.itemId
        if(id==android.R.id.home)     //R.id.home is id of hamburger icon since it is placed on home button
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun  opendashboard()
    {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout, Dashboard())
            .commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }


    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is Dashboard -> opendashboard()
            else -> super.onBackPressed()
        }
    }

}