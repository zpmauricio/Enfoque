package net.it96.enfoque.fragments.list

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
import kotlinx.android.synthetic.main.fragment_project_detail.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.databinding.FragmentProjectListBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource
import timber.log.Timber


@Suppress("UNCHECKED_CAST")
class ProjectListFragment : Fragment(), ProjectListAdapter.OnProjectClickListener {

    private lateinit var binding: FragmentProjectListBinding

//    private lateinit var projectViewModel: ProjectViewModel
    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl()) }

    private lateinit var recyclerView: RecyclerView


    companion object {
        private const val ARG_OBJECT = "object"
    }

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

        return binding.root
    }

    private fun setupRecyclerView() {
        Timber.i("***MZP*** setupRecyclerView")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("***onViewCreated***")

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
////            val tabItem: TabItem = binding.
        }
    }

//    private fun observeData() {
//        binding.shimmerViewContainer.startShimmer()
//        projectViewModel.fetchAllProjectsData().observe(viewLifecycleOwner, {
//            binding.shimmerViewContainer.visibility = View.GONE
//            binding.shimmerViewContainer.stopShimmer()
//            adapter.setListData(it)
//            adapter.notifyDataSetChanged()
//        })
//    }

    private fun observeData() {
        Timber.i("***MZP*** observeData")
        binding.shimmerViewContainer.startShimmer()
//        projectViewModel.fetchAllProjectsData().observe(viewLifecycleOwner, Observer { result ->
//            when (result) {
//                is Resource.Loading<*> -> {
//                    progressBar.visibility = View.VISIBLE
//                }
//                is Resource.Success<*> -> {
//                    progressBar.visibility = View.GONE
//                    recyclerView.adapter = ProjectListAdapter(requireContext(), result.data, this)
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
            Timber.i("***MZP*** projectViewModel.getProjectList")
            Timber.i("***MZP*** %s", result.toString())
            when (result) {
                is Resource.Loading<*> -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success<*> -> {
                    Timber.i("***MZP*** RESOURCE.SUCCESS")
                    progressBar.visibility = View.GONE
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.shimmerViewContainer.stopShimmer()
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

    override fun onResume() {
        super.onResume()
        Timber.i("***MZP*** onResume")
    }

    override fun onProjectClick(project: Project) {
        Timber.i("***MZP*** onProjectClick")
        val bundle = Bundle()
        bundle.putParcelable("Project", project)
        Timber.i("***MZP*** project $project")
        findNavController().navigate(R.id.projectDetailFragment, bundle)
    }
}