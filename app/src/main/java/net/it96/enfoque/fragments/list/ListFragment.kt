package net.it96.enfoque.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.view.*
import net.it96.enfoque.R
import net.it96.enfoque.data.ProjectViewModel
import net.it96.enfoque.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding : FragmentListBinding

    private lateinit var mProjectViewModel: ProjectViewModel

    private lateinit var viewModel: ListFragmentViewModel

//    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(ListFragmentViewModel::class.java)
        // Set the viewModel for dataBinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.listFragmentViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_list, container, false)
//        viewPager = binding.viewPager
//        setUpTabs()

        // Recyclerview
        val adapter = ListAdapter()
        val recyclerView = view?.recyclerview

        if (recyclerView != null) {
            recyclerView.adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }


        // ProjectViewModel
        mProjectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        mProjectViewModel.readAllData.observe(viewLifecycleOwner, { project ->
            adapter.setData(project)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToAddFragment2())
        }

        return binding.root
    }

//    private fun setUpTabs() {
//        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)
//        fragmentAdapter.addFragment(ListFragment(), "Projects")
////        adapter.addFragment(TodayFragment(), "Favourites")
////        adapter.addFragment(SettingsFragment(), "Settings")
//        viewPager.adapter = fragmentAdapter
//        tabs.setupWithViewPager(viewPager)
////
////        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
//    }

}