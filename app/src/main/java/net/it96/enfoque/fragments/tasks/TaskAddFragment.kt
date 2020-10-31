package net.it96.enfoque.fragments.tasks

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.database.Task
import net.it96.enfoque.databinding.AddTaskBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory

class TaskAddFragment : DialogFragment() {

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl(), "") }

    private lateinit var selectedProject: Project

    private lateinit var binding: AddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate view and obtain an instance of the binding class
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_task,
            container,
            false
        )

        // Call View Model and send the data to be stored
        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        binding.btnSaveNewTask.setOnClickListener {
            insertDataToDatabase()
        }

        binding.activeProject = selectedProject

        return binding.root
    }

    private fun insertDataToDatabase() {
        val newTask = binding.etxtNewTask.text.toString()

        if (inputCheck(newTask)) {

            if (newTask.isEmpty())
            {
                binding.etxtNewTask.error = "Please enter a task"
                return
            }

            // Create new task
            val task = Task(newTask).apply { }

            projectViewModel.addTask(task, selectedProject)
            val adapter = TasksAdapter(requireContext(), projectViewModel)
            adapter.addTask(task)

            // Navigate Back
            dismiss()
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(task: String): Boolean {
        return !(TextUtils.isEmpty(task))
    }
}