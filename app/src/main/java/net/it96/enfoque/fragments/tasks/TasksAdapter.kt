package net.it96.enfoque.fragments.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import net.it96.enfoque.database.Task
import net.it96.enfoque.databinding.FragmentProjectDetailBinding
import net.it96.enfoque.databinding.FragmentTodayBinding
import net.it96.enfoque.databinding.TaskRowBinding
import net.it96.enfoque.viewmodels.ProjectViewModel

class TasksAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Task? = null
    var topId : Int = 0

    private var tasksData = mutableListOf<Task>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TasksAdapter.TasksViewHolder {
        val itemBinding = TaskRowBinding.inflate(LayoutInflater.from(context), parent, false)

        return TasksViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TasksAdapter.TasksViewHolder, position: Int) {
        val currentItem = tasksData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = tasksData.size

    inner class TasksViewHolder(val binding: TaskRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(task: Task) {
            binding.txtTask.text = task.description
            binding.txtDate.text = task.date
            binding.etxtEditTask.setText(task.description)
            binding.editTaskDate.text = task.date
            if(task.id.toInt() > topId) topId = task.id.toInt()

            binding.cvRowTasks.setOnClickListener {
                it.visibility = View.GONE
                binding.cvRowEditTasks.visibility = View.VISIBLE
            }

            binding.ibSaveEditTask.setOnClickListener {
                task.description = binding.etxtEditTask.text.toString()
                task.date = binding.editTaskDate.text.toString()
                projectViewModel.editTask(task)
                binding.cvRowEditTasks.visibility = View.GONE
                binding.cvRowTasks.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            val builder : MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

            builder.setTitleText("Select a Date")
            val picker : MaterialDatePicker<*> = builder.build()

            binding.editTaskDate.setOnClickListener {
                picker.show((context as AppCompatActivity).supportFragmentManager, picker.toString())
            }

            picker.addOnPositiveButtonClickListener {
                binding.editTaskDate.text = picker.headerText
            }

        }
    }

    fun setListData(data: MutableList<Task>) {
        tasksData = data
    }

    fun getObject(pos: Int) : Task {
        return tasksData[pos]
    }

    fun addTask(task: Task) {
        tasksData.add(task)
    }

    fun deleteTask(task: Task, viewHolder: RecyclerView.ViewHolder, fragmentProjectDetailBinding : FragmentProjectDetailBinding? = null, fragmentTodayBinding: FragmentTodayBinding?) {
        removedPosition = viewHolder.adapterPosition
        removedItem = tasksData[removedPosition]

        tasksData.removeAt(removedPosition)
        projectViewModel.deleteTask(task)
        notifyItemRemoved(removedPosition)

        if(fragmentProjectDetailBinding != null) {
            Snackbar.make(fragmentProjectDetailBinding.clDetailMainLayout,
                "${removedItem!!.description} deleted.",
                Snackbar.LENGTH_LONG).setAction("UNDO") {
                projectViewModel.addTask(task)
                addTask(removedItem!!)
                notifyDataSetChanged()
            }.show()
        } else if(fragmentTodayBinding != null) {
            Snackbar.make(fragmentTodayBinding.clTodayLayout,
                "${removedItem!!.description} deleted.",
                Snackbar.LENGTH_LONG).setAction("UNDO") {
                projectViewModel.addTask(task)
                addTask(removedItem!!)
                notifyDataSetChanged()
            }.show()
        }
    }
}
