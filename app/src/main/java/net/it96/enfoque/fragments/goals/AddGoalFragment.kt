package net.it96.enfoque.fragments.goals

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.add_goals.*
import kotlinx.android.synthetic.main.add_goals.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Goal
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import timber.log.Timber

class AddGoalFragment : DialogFragment() {

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl(), "") }

    private lateinit var selectedProject: Project

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.add_goals, container, false)
        view.btn_saveNewGoal.setOnClickListener {
            insertDataToDatabase(savedInstanceState)
        }

        return view
    }

    private fun insertDataToDatabase(savedInstanceState: Bundle?) {
        val newGoal = etxt_newGoal.text.toString()

        if (inputCheck(newGoal)) {

            if (newGoal.isEmpty())
            {
                etxt_newGoal.error = "Please enter a goal"
                return
            }

            // Create new goal
            val goal = Goal(newGoal).apply { }

            // Call View Model and send the data to be stored
                requireArguments().let {
                selectedProject = it.getParcelable("Project")!!
            }

            Timber.i("***MZP*** Project: $selectedProject")
            projectViewModel.addGoal(goal, selectedProject)

            // Navigate Back
            dismiss()
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(goal: String): Boolean {
        return !(TextUtils.isEmpty(goal))
    }

}