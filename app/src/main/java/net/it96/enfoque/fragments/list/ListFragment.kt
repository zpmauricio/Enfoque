package net.it96.enfoque.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.view.*
import net.it96.enfoque.R
import net.it96.enfoque.data.ProjectViewModel

class ListFragment : Fragment() {

    private lateinit var mProjectViewModel: ProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Recyclerview
        val adapter = ListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ProjectViewModel
        mProjectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        mProjectViewModel.readAllData.observe(viewLifecycleOwner, Observer { project ->
            adapter.setData(project)
        })

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment2)
        }

        return view
    }

}