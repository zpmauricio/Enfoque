package net.it96.enfoque.fragments.tasks

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.database.Task
import net.it96.enfoque.databinding.AddTaskBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import java.util.*

class TaskAddFragment : DialogFragment() {

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(
        ProjectRepositoryImpl(),
        "") }

    private lateinit var selectedProject: Project

    private lateinit var binding: AddTaskBinding

    private var topId : Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
            topId = it.getInt("topId")
        }

        binding.ibSaveNewTask.setOnClickListener {
            insertDataToDatabase()
        }

        binding.activeProject = selectedProject

        val builder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

//        val now = Calendar.getInstance()
//        val myDate : Date = Date(now.timeInMillis)
//        val dateInMillis : Long = myDate.time
//        builder.setSelection()

        builder.setTitleText("Select a Date")
        val picker : MaterialDatePicker<*> = builder.build()

        // Format the current time.
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
        val currentDate = Date()
        val dateString: String = formatter.format(currentDate)

        binding.addTaskDate.text = dateString

        binding.addTaskDate.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }

        picker.addOnPositiveButtonClickListener {
            binding.addTaskDate.text = picker.headerText
        }

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
            val task = Task("${topId + 1}",
                newTask,
                binding.addTaskDate.text.toString(),
                selectedProject.name,
                FirebaseAuth.getInstance().currentUser!!.email.toString()).apply { }

            projectViewModel.addTask(task)
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