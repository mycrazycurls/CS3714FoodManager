package com.example.foodieplanner

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodieplanner.models.CalendarDay
import com.example.foodieplanner.models.Day
import com.example.foodieplanner.models.Meal

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: Model by viewModels()

        var database: DatabaseReference = Firebase.database.reference

        // Firebase Days Listener
        val daysListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var today = MaterialDatePicker.todayInUtcMilliseconds()
                var days = arrayListOf<Day>()
                for (snapShot in dataSnapshot.children) {
                    val day = snapShot.getValue<Day>()
                    val time = day?.day?.timeInMiliSeconds
                    if (day != null && time != null && time >= today) {
                        days.add(day)
                    }
                }
                val size = days.size
                loadRemainingDays(days)
                if (size != days.size) {
                    database.child("Days").setValue(days)
                }
                viewModel.days.postValue(days)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("MainRetrieveData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("Days").addValueEventListener(daysListener)

        // Firebase Meals Listener
        val mealsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var savedMeals = arrayListOf<Meal>()
                for (snapShot in dataSnapshot.children) {
                    val meal = snapShot.getValue<Meal>()
                    if (meal != null) {
                        savedMeals.add(meal)
                    }
                }
                viewModel.savedMeals.postValue(savedMeals)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("MainRetrieveData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("Meals").addValueEventListener(mealsListener)

        // Firebase Albums Listener
        val albumsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var albums = arrayListOf<String>()
                for (ss in snapshot.children) {
                    val album = ss.getValue<String>()
                    if (album != null) {
                        albums.add(album)
                    }
                }
                viewModel.albums.postValue(albums)
            }
            override fun onCancelled(error: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("MainRetrieveData", "loadPost:onCancelled", error.toException())
            }
        }
        database.child("Albums").addValueEventListener(albumsListener)


        // toolbar
        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)

        // nav host
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        // nav controller
        val navController = host.navController

        //val set: Set<Int> = setOf(R.id.homeFragment,R.id.plannerFragment,R.id.savedMealsFragment)
        //appBarConfiguration = AppBarConfiguration(set,null)


        // setup
        //setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            Log.d("NavigationActivity", "Navigated to $dest")
        }


    }

    private fun loadRemainingDays(days: ArrayList<Day>): ArrayList<Day> {

        val month = SimpleDateFormat("MMM", Locale.US)
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.US)
        val dayOfMonth = SimpleDateFormat("dd", Locale.US)

        var today = MaterialDatePicker.todayInUtcMilliseconds()

        var dayLimit: Calendar = Calendar.getInstance()
        dayLimit.timeInMillis = today
        dayLimit.add(Calendar.DAY_OF_MONTH, 7)

        var latestDay = days[days.size-1]
        var latestTime = latestDay.day?.timeInMiliSeconds

        var calendar: Calendar = Calendar.getInstance()
        var tracker: Calendar = Calendar.getInstance()
        tracker.timeInMillis = today

        if (latestTime != null) {
            while (tracker.timeInMillis <= latestTime) {
                tracker.add(Calendar.DAY_OF_MONTH,1)
                calendar.add(Calendar.DAY_OF_MONTH,1)
            }
        }

        while (tracker.timeInMillis <= dayLimit.timeInMillis) {
            var day = Day(meals = null,
                CalendarDay(
                    month.format(calendar.timeInMillis),
                    dayOfWeek.format(calendar.timeInMillis),
                    dayOfMonth.format(calendar.timeInMillis),
                    tracker.timeInMillis),
                false
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            tracker.add(Calendar.DAY_OF_MONTH, 1)
            //adapter.addDay(day)
            //viewModel.addDay(day)
            days.add(day)
        }
        return days
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        // TODO STEP 9.4 - Use NavigationUI to set up a Navigation View
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
//        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
//        sideNavView?.setupWithNavController(navController)
        // TODO END STEP 9.4
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val retValue = super.onCreateOptionsMenu(menu)
////        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        // The NavigationView already has these same navigation items, so we only add
//        // navigation items to the menu here if there isn't a NavigationView
//
////        if (navigationView == null) {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
////        }
////        return retValue
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        // Have the NavigationUI look for an action or destination matching the menu
////        // item id and navigate there if found.
////        // Otherwise, bubble up to the parent.
//        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
//                || super.onOptionsItemSelected(item)
//    }

}


