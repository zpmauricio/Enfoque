package net.it96.enfoque.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.it96.enfoque.database.*
import net.it96.enfoque.databinding.FragmentProjectDetailBinding
import net.it96.enfoque.fragments.goals.GoalAddFragment
import net.it96.enfoque.fragments.goals.GoalDelete
import net.it96.enfoque.fragments.goals.GoalsAdapter
import net.it96.enfoque.fragments.notes.NoteAddFragment
import net.it96.enfoque.fragments.notes.NoteDelete
import net.it96.enfoque.fragments.notes.NotesAdapter
import net.it96.enfoque.fragments.results.KeyResultAddFragment
import net.it96.enfoque.fragments.results.KeyResultDelete
import net.it96.enfoque.fragments.results.KeyResultsAdapter
import net.it96.enfoque.fragments.tasks.TaskAddFragment
import net.it96.enfoque.fragments.tasks.TaskDelete
import net.it96.enfoque.fragments.tasks.TasksAdapter
import net.it96.enfoque.viewmodels.ProjectViewModel
import net.it96.enfoque.viewmodels.ViewModelFactory
import net.it96.enfoque.vo.Resource

class ProjectListDetailFragment : Fragment() {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var notesRecyclerView: RecyclerView

    private lateinit var selectedProject : Project

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory(ProjectRepositoryImpl(),
            selectedProject.name)
    }

    private lateinit var goalsList : MutableList<Goal>
    private lateinit var keyResultsList : MutableList<KeyResult>
    private lateinit var tasksList : MutableList<Task>
    private lateinit var notesList : MutableList<Note>

    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var resultsAdapter: KeyResultsAdapter
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var notesAdapter: NotesAdapter

    private var _binding: FragmentProjectDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProjectDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        requireArguments().let {
//            Timber.i("***MZP*** requireArguments")
//            Timber.i("***MZP*** it: ${it}")
//            Timber.i("***MZP*** it: ${it.getParcelable<Project>("Project")!!.name}")
//            view.txt_projectName.text = it.getParcelable<Project>("Project")!!.name
            selectedProject = it.getParcelable("Project")!!
        }

        goalsAdapter = GoalsAdapter(requireContext(), projectViewModel)
        goalsRecyclerView = binding.rvGoals
        goalsRecyclerView.adapter = goalsAdapter

        resultsAdapter = KeyResultsAdapter(requireContext(), projectViewModel)
        resultsRecyclerView = binding.rvKeyResults
        resultsRecyclerView.adapter = resultsAdapter

        tasksAdapter = TasksAdapter(requireContext(), projectViewModel)
        tasksRecyclerView = binding.rvTasks
        tasksRecyclerView.adapter = tasksAdapter

        notesAdapter = NotesAdapter(requireContext(), projectViewModel)
        notesRecyclerView = binding.rvNotes
        notesRecyclerView.adapter = notesAdapter

        // Start process to read from the database
        observeGoalsData()
        observeKeyResultsData()
        observeTasksData()
        observeNotesData()

        clickListeners()

        (requireActivity() as AppCompatActivity).supportActionBar?.title = selectedProject.name

        return view
    }

    private fun clickListeners() {
        binding.cvGoals.setOnClickListener {
            val dialog = GoalAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            bundle.putInt("topId", goalsAdapter.topId)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddGoal")
        }

        binding.cvResults.setOnClickListener {
            val dialog = KeyResultAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            bundle.putInt("topId", resultsAdapter.topId)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddKeyResult")
        }

        binding.cvTasks.setOnClickListener {
            val dialog = TaskAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            bundle.putInt("topId", tasksAdapter.topId)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddTask")
        }

        binding.cvNotes.setOnClickListener {
            val dialog = NoteAddFragment()
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            bundle.putInt("topId", notesAdapter.topId)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "AddNote")
        }

        binding.llGoals.setOnClickListener {
            if(binding.ivDownArrowGoals.isVisible) binding.ivDownArrowGoals.visibility = View.GONE else binding.ivDownArrowGoals.visibility = View.VISIBLE
            if(binding.ivUpArrowGoals.isVisible) binding.ivUpArrowGoals.visibility = View.GONE else binding.ivUpArrowGoals.visibility = View.VISIBLE
            if(binding.llCollapseGoals.isVisible) binding.llCollapseGoals.visibility = View.GONE else binding.llCollapseGoals.visibility = View.VISIBLE
        }

        binding.llKeyResults.setOnClickListener {
            if(binding.ivDownArrowKeyResults.isVisible) binding.ivDownArrowKeyResults.visibility = View.GONE else binding.ivDownArrowKeyResults.visibility = View.VISIBLE
            if(binding.ivUpArrowKeyResults.isVisible) binding.ivUpArrowKeyResults.visibility = View.GONE else binding.ivUpArrowKeyResults.visibility = View.VISIBLE
            if(binding.llCollapseKeyResults.isVisible) binding.llCollapseKeyResults.visibility = View.GONE else binding.llCollapseKeyResults.visibility = View.VISIBLE
        }

        binding.llTasks.setOnClickListener {
            if(binding.ivDownArrowTasks.isVisible) binding.ivDownArrowTasks.visibility = View.GONE else binding.ivDownArrowTasks.visibility = View.VISIBLE
            if(binding.ivUpArrowTasks.isVisible) binding.ivUpArrowTasks.visibility = View.GONE else binding.ivUpArrowTasks.visibility = View.VISIBLE
            if(binding.llCollapseTasks.isVisible) binding.llCollapseTasks.visibility = View.GONE else binding.llCollapseTasks.visibility = View.VISIBLE
        }

        binding.llNotes.setOnClickListener {
            if(binding.ivDownArrowNotes.isVisible) binding.ivDownArrowNotes.visibility = View.GONE else binding.ivDownArrowNotes.visibility = View.VISIBLE
            if(binding.ivUpArrowNotes.isVisible) binding.ivUpArrowNotes.visibility = View.GONE else binding.ivUpArrowNotes.visibility = View.VISIBLE
            if(binding.llCollapseNotes.isVisible) binding.llCollapseNotes.visibility = View.GONE else binding.llCollapseNotes.visibility = View.VISIBLE
        }
    }

    private fun observeGoalsData() {
        projectViewModel.getGoalsList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    goalsList = result.data as MutableList<Goal>
                    goalsAdapter.setListData(goalsList)
                    goalsRecyclerView.adapter = goalsAdapter

                    val itemTouchHelper = ItemTouchHelper(GoalDelete(goalsRecyclerView.adapter as GoalsAdapter, selectedProject, requireContext(), binding))
                    itemTouchHelper.attachToRecyclerView(goalsRecyclerView)

                    goalsAdapter.notifyDataSetChanged()
                }
                is Resource.Failure<*> -> {
                    Toast.makeText(
                        requireContext(),
                        "Error al traer los datos ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun observeKeyResultsData() {
        projectViewModel.getKeyResultsList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    keyResultsList = result.data as MutableList<KeyResult>
                    resultsAdapter.setListData(keyResultsList)
                    resultsRecyclerView.adapter = resultsAdapter

                    val itemTouchHelper = ItemTouchHelper(KeyResultDelete(resultsRecyclerView.adapter as KeyResultsAdapter, selectedProject, requireContext(), binding))
                    itemTouchHelper.attachToRecyclerView(resultsRecyclerView)

                    resultsAdapter.notifyDataSetChanged()
                }
                is Resource.Failure<*> -> {
                    Toast.makeText(
                        requireContext(),
                        "Error al traer los datos ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun observeTasksData() {
        projectViewModel.getTasksList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    tasksList = result.data as MutableList<Task>
                    tasksAdapter.setListData(tasksList)
                    tasksRecyclerView.adapter = tasksAdapter

                    val itemTouchHelper = ItemTouchHelper(TaskDelete(tasksRecyclerView.adapter as TasksAdapter, requireContext(), binding))
                    itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

                    tasksAdapter.notifyDataSetChanged()
                }
                is Resource.Failure<*> -> {
                    Toast.makeText(
                        requireContext(),
                        "Error al traer los datos ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun observeNotesData() {
        projectViewModel.getNotesList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading<*> -> {

                }
                is Resource.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    notesList = result.data as MutableList<Note>
                    notesAdapter.setListData(notesList)
                    notesRecyclerView.adapter = notesAdapter

                    val itemTouchHelper = ItemTouchHelper(NoteDelete(notesRecyclerView.adapter as NotesAdapter, selectedProject, requireContext(), binding))
                    itemTouchHelper.attachToRecyclerView(notesRecyclerView)

                    notesAdapter.notifyDataSetChanged()
                }
                is Resource.Failure<*> -> {
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