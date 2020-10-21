package net.it96.enfoque.fragments.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_project_detail.*
import net.it96.enfoque.LoginActivity
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.databinding.FragmentProjectListBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource


@Suppress("UNCHECKED_CAST")
class ProjectListFragment : Fragment(), ProjectListAdapter.OnProjectClickListener {

    private lateinit var binding: FragmentProjectListBinding

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl(), "") }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_project_list,
            container,
            false
        )

        // Set the viewModel for dataBinding - this allows the bound layout access
        // to all the data in the ViewModel
//        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        binding.projectViewModel = projectViewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Recyclerview
        recyclerView = binding.recyclerView
        setupRecyclerView()

        // Start process to read from the database
        observeData()

        binding.floatingActionButton.setOnClickListener {
            findNavController(this).navigate(ProjectListFragmentDirections.actionListFragmentToAddFragment())
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }

        return binding.root
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
//        binding.shimmerViewContainer.startShimmer()

//        projectViewModel.getProjectList.observe(viewLifecycleOwner, { result ->
//            when (result) {
//                is Resource.Loading<*> -> {
//                    progressBar.visibility = View.VISIBLE
//                }
//                is Resource.Success<*> -> {
//                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
//                    recyclerView.adapter = ProjectListAdapter(requireContext(),
//                        result.data as List<Project>, this)
//                }
//                is Resource.Failure<*> -> {
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(
//                        requireContext(),
//                        "Error al traer los datos ${result.exception}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        })

        projectViewModel.getProjectList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
                    recyclerView.adapter = ProjectListAdapter(requireContext(),
                        result.data as List<Project>, this)
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

    override fun onProjectClick(project: Project) {
        val bundle = Bundle()
        bundle.putParcelable("Project", project)
        findNavController().navigate(R.id.projectDetailFragment, bundle)
    }

    private fun signOut(){
        AuthUI.getInstance().signOut(requireActivity()).addOnSuccessListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().supportFinishAfterTransition()
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Ocurrio un error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}