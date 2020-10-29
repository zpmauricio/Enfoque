package net.it96.enfoque.fragments.results

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_results.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.KeyResult
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource
import timber.log.Timber

class KeyResultsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var selectedProject: Project

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory(ProjectRepositoryImpl(),
            selectedProject.name)
    }

    private lateinit var deleteIcon: Drawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        recyclerView = view.rv_keyResults
        setupRecyclerView()

        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        // Start process to read from the database
        observeData()

        view.btn_tasks.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            findNavController().navigate(R.id.action_resultsFragment_to_tasksFragment, bundle)
        }

        deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!

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
        projectViewModel.getKeyResultsList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
//                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
                    @Suppress("UNCHECKED_CAST")
                    recyclerView.adapter = KeyResultsAdapter(requireContext(),
                        result.data as List<KeyResult>, projectViewModel)
                    Timber.i("***MZP*** result: $result")

                    val itemTouchHelper = ItemTouchHelper(KeyResultsDelete(recyclerView.adapter as KeyResultsAdapter, selectedProject, requireContext()))
                    itemTouchHelper.attachToRecyclerView(recyclerView)
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