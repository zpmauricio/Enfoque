package net.it96.enfoque.fragments.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.database.Task
import net.it96.enfoque.databinding.FragmentTodayBinding
import net.it96.enfoque.fragments.tasks.TaskDelete
import net.it96.enfoque.fragments.tasks.TasksAdapter
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource

class TodayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory(ProjectRepositoryImpl(), "")
    }

    private lateinit var tasksList : MutableList<Task>

    private lateinit var adapter: TasksAdapter

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)

        setupRecyclerView()

        observeData()

        clickListeners()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = TasksAdapter(requireContext(), projectViewModel)
        recyclerView = binding.rvToday
        recyclerView.adapter = adapter
    }

    private fun clickListeners() {

    }

    private fun observeData() {
        projectViewModel.getTodayTasksList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    tasksList = result.data as MutableList<Task>
                    adapter.setListData(tasksList)
                    recyclerView.adapter = adapter

                    val itemTouchHelper = ItemTouchHelper(TaskDelete(recyclerView.adapter as TasksAdapter, requireContext(), null, binding))
                    itemTouchHelper.attachToRecyclerView(recyclerView)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}