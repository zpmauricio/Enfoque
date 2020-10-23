package net.it96.enfoque.fragments.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import timber.log.Timber

class GoalsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var selectedProject: Project

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory(ProjectRepositoryImpl(),
            selectedProject.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        recyclerView = view.rv_90daygoal
        setupRecyclerView()

        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        // Start process to read from the database
        observeData()

        view.btn_keyResults.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            findNavController().navigate(R.id.action_ninetyDayGoalsFragment_to_resultsFragment, bundle)
        }

        view.btn_addGoal.setOnClickListener {
            var dialog = AddGoalFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddGoal")
        }

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
                    recyclerView.adapter = GoalsAdapter(requireContext(),
                        result.data as List<Goal>)
                    Timber.i("***MZP*** result: $result")
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