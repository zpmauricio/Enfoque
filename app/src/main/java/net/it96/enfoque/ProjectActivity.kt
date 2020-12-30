package net.it96.enfoque

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import net.it96.enfoque.databinding.ActivityProjectBinding
import timber.log.Timber

class ProjectActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController

    private var _binding: ActivityProjectBinding? = null
    private val binding get() = _binding!!

//    private val adapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        viewPager2.adapter = adapter
//        val tabLayoutMediator = TabLayoutMediator(tab_layout, viewPager2) { tab, position ->
//            when (position + 1) {
//                1 -> { tab.setText(R.string.tab_text_1) }
//                2 -> { tab.setText(R.string.tab_text_2) }
//                else -> tab.setText(R.string.tab_text_1)
//            }
//        }
//        tabLayoutMediator.attach()

        Timber.i("MZP onCreate")

        val toolbar : Toolbar = binding.toolbar
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
             R.id.projectListFragment, R.id.fragment_today
            ,R.id.nav_calendar, R.id.nav_settings, R.id.nav_labels, R.id.nav_priorities
        ), drawerLayout)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            Toast.makeText(this, "Haz algo!!", Toast.LENGTH_SHORT).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun signOut(){
        AuthUI.getInstance().signOut(this).addOnSuccessListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Ocurrio un error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}