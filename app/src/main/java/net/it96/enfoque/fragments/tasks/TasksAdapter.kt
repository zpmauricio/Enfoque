package net.it96.enfoque.fragments.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Task
import timber.log.Timber

class TasksAdapter(
    private val context: Context,
    private var tasksList: List<Task>,
) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TasksAdapter.TasksViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_row, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksAdapter.TasksViewHolder, position: Int) {
        val currentItem = tasksList[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(task: Task) {
            Timber.i("***MZP*** task.description: ${task.description}")
            itemView.txt_task.text = task.description
        }
    }

    fun setListData(data: MutableList<Task>) {
        tasksList = data
    }
}
