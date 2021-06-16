package net.it96.enfoque.fragments.notes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.it96.enfoque.R
import net.it96.enfoque.database.Note
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepositoryImpl
import net.it96.enfoque.databinding.FragmentNotesBinding
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var selectedProject: Project

    private val projectViewModel by viewModels<ProjectViewModel> { ViewModelFactory(ProjectRepositoryImpl(), selectedProject.name) }

    private lateinit var notesList : MutableList<Note>

    private lateinit var deleteIcon: Drawable

    private lateinit var adapter: NotesAdapter

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        requireArguments().let {
            selectedProject = it.getParcelable("Project")!!
        }

        adapter = NotesAdapter(requireContext(), projectViewModel)
        recyclerView = binding.rvNotes
        setupRecyclerView()

        // Start process to read from the database
        observeData()

        binding.btnProjects.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_projectListFragment)
        }

        binding.btnAddNote.setOnClickListener {
            val dialog = NoteAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddNote")
        }

        deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeData() {
        projectViewModel.getNotesList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
//                    progressBar.visibility = View.GONE
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.shimmerViewContainer.stopShimmer()
                    @Suppress("UNCHECKED_CAST")
                    notesList = result.data as MutableList<Note>
                    adapter.setListData(notesList)
                    recyclerView.adapter = adapter

//                    val itemTouchHelper = ItemTouchHelper(NoteDelete(recyclerView.adapter as NotesAdapter, selectedProject, requireContext()))
//                    itemTouchHelper.attachToRecyclerView(recyclerView)

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
}