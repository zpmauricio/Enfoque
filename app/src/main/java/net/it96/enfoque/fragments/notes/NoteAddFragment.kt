package net.it96.enfoque.fragments.notes

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
import net.it96.enfoque.database.Note
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.databinding.AddNoteBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory

class NoteAddFragment : DialogFragment() {

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl(), "") }

    private lateinit var selectedProject: Project

    private lateinit var binding: AddNoteBinding

    private var topId : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate view and obtain an instance of the binding class
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_note,
            container,
            false
        )

        // Call View Model and send the data to be stored
        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
            topId = it.getInt("topId")
        }

        binding.btnSaveNewNote.setOnClickListener {
            insertDataToDatabase()
        }

        binding.activeProject = selectedProject

        return binding.root
    }

    private fun insertDataToDatabase() {
        val newNote = binding.etxtNewNote.text.toString()

        if (inputCheck(newNote)) {

            if (newNote.isEmpty())
            {
                binding.etxtNewNote.error = "Please enter a Note"
                return
            }

            // Create new note
            val note = Note("${topId + 1}", newNote, selectedProject.name).apply { }

            projectViewModel.addNote(note)
            val adapter = NotesAdapter(requireContext(), projectViewModel)
            adapter.addNote(note)

            // Navigate Back
            dismiss()
        }
        else
        {
            Toast.makeText(requireContext(), R.string.FillRequiredFields, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(note: String): Boolean {
        return !(TextUtils.isEmpty(note))
    }
}