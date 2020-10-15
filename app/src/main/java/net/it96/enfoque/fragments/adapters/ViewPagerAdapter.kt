package net.it96.enfoque.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.it96.enfoque.fragments.list.ProjectListFragment
import net.it96.enfoque.fragments.today.TodayFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position + 1) {
            1 -> {
                ProjectListFragment()
            }
            2 -> {
                TodayFragment()
            }
            else -> throw IllegalStateException("Invalid adapter position")
        }
//        val fragment = ProjectListFragment()
////        fragment.arguments = Bundle().apply {
////            // Our object is just an integer :-P
////            putInt(ARG_OBJECT, position + 1)
////        }
//        return fragment
    }
}

//class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
//
//    companion object {
//        private const val ARG_OBJECT = "object"
//    }
//
//    override fun getItemCount(): Int = 2
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position + 1) {
//            1 -> {
//                ProjectListFragment()
//            }
//            2 -> {
//                TodayFragment()
//            }
//            else -> ProjectListFragment()
//        }
////        val fragment = ProjectListFragment()
//////        fragment.arguments = Bundle().apply {
//////            // Our object is just an integer :-P
//////            putInt(ARG_OBJECT, position + 1)
//////        }
////        return fragment
//    }
//
//}

//class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
//    FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//    private val mFragmentList = ArrayList<Fragment>()
//    private val mFragmentTitleList = ArrayList<String>()
//
//    override fun getItem(position: Int): Fragment {
//        return mFragmentList[position]
//    }
//
//    override fun getCount(): Int {
//        return mFragmentList.size
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return mFragmentTitleList[position]
//    }
//
//    fun addFragment(fragment: Fragment, title: String) {
//        mFragmentList.add(fragment)
//        mFragmentTitleList.add(title)
//    }
//}