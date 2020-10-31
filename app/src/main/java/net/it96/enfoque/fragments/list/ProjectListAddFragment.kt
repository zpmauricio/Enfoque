package net.it96.enfoque.fragments.list

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.project_add.*
import kotlinx.android.synthetic.main.project_add.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory

class ProjectListAddFragment : Fragment() {

//    private lateinit var mProjectViewModel: ProjectViewModel
    private val mProjectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(
        ProjectRepositoryImpl(), "") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.project_add, container, false)

//        mProjectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        view.addProject_btn.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val projectName = addProjectName.text.toString()
//        val rdButton = if (rb_img1.isSelected) {img_project1} else if (rb_img2.isSelected) { img_project2 } else img_project3
//        val results = addResults.text.toString()
//        val goals90 = addGoals90.text.toString()
//        val goals2W = addGoals2W.text.toString()
//        val actions = addActions.text.toString()
//        val notes = addNotes.text.toString()

        if (inputCheck(projectName)) {

            if (projectName.isEmpty())
            {
                addProjectName.error = "Please enter a name"
                return
            }

            // Create new project
            val project = Project("", projectName, "", arrayListOf(), "", "").apply { }

            // Call View Model and send the data to be stored
            mProjectViewModel.addProject(project)

            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(projectName: String): Boolean {
        return !(TextUtils.isEmpty(projectName))
    }

}