package net.it96.enfoque

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import timber.log.Timber

class ProjectActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController

//    private val adapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
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

//        val toolbar : Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)
//        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.fragment_today,
//                R.id.projectListFragment
//            ), drawerLayout
//        )

//        supportFragmentManager.beginTransaction()
//            .add(R.id.navHostFragment, ProjectListFragment())
//            .commit()

//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navigationView.setupWithNavController(navController)


//        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
//        fab.setOnClickListener {
//            navController.navigate(ProjectListFragmentDirections.actionListFragmentToAddFragment2())
//        }


//        val tabLayout = this.tab_layout
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                // Handle tab select
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                // Handle tab reselect
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                // Handle tab unselect
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.navHostFragment)
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

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

//    private fun signOut(){
//        AuthUI.getInstance().signOut(this).addOnSuccessListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }.addOnFailureListener{
//            Toast.makeText(this, "Ocurrio un error ${it.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
}