package net.it96.enfoque.fragments.goals

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_goals.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Goal
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource

class GoalsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var selectedProject: Project

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory(ProjectRepositoryImpl(),
            selectedProject.name)
    }

    private lateinit var goalsList : MutableList<Goal>

    private lateinit var deleteIcon: Drawable

    private lateinit var adapter: GoalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        adapter = GoalsAdapter(requireContext(), projectViewModel)
        recyclerView = view.rv_goal
        setupRecyclerView()

        // Start process to read from the database
        observeData()

        view.btn_keyResults.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            findNavController().navigate(R.id.action_ninetyDayGoalsFragment_to_resultsFragment, bundle)
        }

        view.btn_addGoal.setOnClickListener {
            val dialog = GoalAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddGoal")
        }

        deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeData() {
        projectViewModel.getGoalsList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
//                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
                    @Suppress("UNCHECKED_CAST")
                    goalsList = result.data as MutableList<Goal>
                    adapter.setListData(goalsList)
                    recyclerView.adapter = adapter

//                    val itemTouchHelper = ItemTouchHelper(GoalDelete(recyclerView.adapter as GoalsAdapter, selectedProject, requireContext()))
//                    itemTouchHelper.attachToRecyclerView(recyclerView)

                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure<*> -> {
//                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error al traer los datos ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}