package net.it96.enfoque

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_list.*
import net.it96.enfoque.fragments.adapters.ViewPagerAdapter
import net.it96.enfoque.fragments.list.ListFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        viewPager = findViewById(R.id.viewPager)
        setupActionBarWithNavController(navController)
        setUpTabs()
        Timber.i("MZP onCreate")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUpTabs() {
        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(ListFragment(), "Projects")
//        adapter.addFragment(TodayFragment(), "Favourites")
//        adapter.addFragment(SettingsFragment(), "Settings")
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)
//
//        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
    }
}