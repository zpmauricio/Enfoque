package net.it96.enfoque.fragments.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.Task
import net.it96.enfoque.viewmodels.ProjectViewModel

class TasksAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Task? = null

    private var tasksData = mutableListOf<Task>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TasksAdapter.TasksViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_row, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksAdapter.TasksViewHolder, position: Int) {
        val currentItem = tasksData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = tasksData.size

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(task: Task) {
            itemView.txt_task.text = task.description
        }
    }

    fun setListData(data: MutableList<Task>) {
        tasksData = data
    }

    fun getObject(pos: Int) : Task {
        return tasksData[pos]
    }

    fun addTask(task: Task) {
        tasksData.toMutableList().add(task)
        notifyDataSetChanged()
    }

    fun deleteTask(task: Task, selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = tasksData[removedPosition]

        tasksData.removeAt(removedPosition)
        projectViewModel.deleteTask(task, selectedProject)
        notifyItemRemoved(removedPosition)


        Snackbar.make(viewHolder.itemView, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addTask(task, selectedProject)
            tasksData.toMutableList().add(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}
