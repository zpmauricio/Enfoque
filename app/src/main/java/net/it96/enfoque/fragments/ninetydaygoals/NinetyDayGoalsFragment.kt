package net.it96.enfoque.fragments.ninetydaygoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_ninety_day_goals.view.*
import kotlinx.android.synthetic.main.fragment_project_detail.*
import net.it96.enfoque.R
import net.it96.enfoque.database.NinetyDayGoal
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource
import timber.log.Timber

class NinetyDayGoalsFragment : Fragment(), NinetyDayGoalsAdapter.OnGoalClickListener {

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
        val view = inflater.inflate(R.layout.fragment_ninety_day_goals, container, false)

        recyclerView = view.rv_90daygoal
        setupRecyclerView()

        requireArguments().let {
            Timber.i("***MZP*** requireArguments")
            Timber.i("***MZP*** it: ${it}")
            Timber.i("***MZP*** it: ${it.getParcelable<Project>("Project")!!.name}")
            //view.txt_projectName.text = it.getParcelable<Project>("Project")!!.name
            selectedProject = it.getParcelable("Project")!!
            Timber.i("Detail Frag: $selectedProject")
        }

        // Start process to read from the database
        observeData()

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
                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
                    recyclerView.adapter = NinetyDayGoalsAdapter(requireContext(),
                        result.data as List<NinetyDayGoal>, this)
                    Timber.i("***MZP*** result: $result")
                }
                is Resource.Failure<*> -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error al traer los datos ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onGoalClick(goal: NinetyDayGoal) {
        val bundle = Bundle()
        bundle.putParcelable("Goal", goal)
//        findNavController().navigate(R.id.projectDetailFragment, bundle)
    }
}