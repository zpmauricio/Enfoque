package net.it96.enfoque.fragments.results

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
import net.it96.enfoque.database.KeyResult
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.databinding.AddKeyResultBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory

class KeyResultAddFragment : DialogFragment() {

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(
        ProjectRepositoryImpl(), "") }

    private lateinit var selectedProject: Project

    private lateinit var binding: AddKeyResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate view and obtain an instance of the binding class
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_key_result,
            container,
            false
        )

        // Call View Model and send the data to be stored
        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        binding.btnSaveNewKeyResult.setOnClickListener {
            insertDataToDatabase()
        }

        binding.activeProject = selectedProject

        return binding.root
    }

    private fun insertDataToDatabase() {
        val newKeyResult = binding.etxtNewKeyResult.text.toString()

        if (inputCheck(newKeyResult)) {

            if (newKeyResult.isEmpty())
            {
                binding.etxtNewKeyResult.error = "Please enter a Key Result"
                return
            }

            // Create new Key Result
            val keyResult = KeyResult(newKeyResult).apply { }

            projectViewModel.addKeyResult(keyResult, selectedProject)
            val adapter = KeyResultsAdapter(requireContext(), projectViewModel)
            adapter.addKeyResult(keyResult)

            // Navigate Back
            dismiss()
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(keyResult: String): Boolean {
        return !(TextUtils.isEmpty(keyResult))
    }
}