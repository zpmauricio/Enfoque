package net.it96.enfoque.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import net.it96.enfoque.R
import net.it96.enfoque.data.Project
import net.it96.enfoque.data.ProjectViewModel

class AddFragment : Fragment() {

    private lateinit var mProjectViewModel: ProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)

        mProjectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        view.addProject_btn.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val projectName = addProjectName.text.toString()
        val results = addResults.text.toString()
        val goals90 = addGoals90.text.toString()
        val goals2W = addGoals2W.text.toString()
        val actions = addActions.text.toString()
        val notes = addNotes.text.toString()

        if (inputCheck(projectName, results, goals90, goals2W, actions, notes)) {
            // Create Project Object
            val project = Project(0, projectName, results, goals90, goals2W, actions, notes)

            // Add Data to Database
            mProjectViewModel.addProject(project)
            Toast.makeText(requireContext(), R.string.SucessfullyAdded, Toast.LENGTH_LONG).show()

            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment2)
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(projectName: String, results: String, goals90: String, goals2W: String, actions: String, notes: String): Boolean {
        return !(TextUtils.isEmpty(projectName) && TextUtils.isEmpty(results) && TextUtils.isEmpty(goals90) && TextUtils.isEmpty(goals2W) && TextUtils.isEmpty(actions) && TextUtils.isEmpty(notes))
    }

}