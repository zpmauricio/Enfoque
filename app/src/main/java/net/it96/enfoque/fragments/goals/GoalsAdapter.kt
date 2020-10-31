package net.it96.enfoque.fragments.goals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.goal_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Goal
import net.it96.enfoque.database.Project
import net.it96.enfoque.viewmodels.ProjectViewModel

class GoalsAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Goal? = null

    private var goalsData = mutableListOf<Goal>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAdapter.GoalsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_row, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalsAdapter.GoalsViewHolder, position: Int) {
        val currentItem = goalsData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = goalsData.size

    inner class GoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(goal: Goal) {
            itemView.txt_goal.text = goal.description
        }
    }

    fun setListData(data: MutableList<Goal>) {
        goalsData = data
    }

    fun getObject(pos: Int) : Goal {
        return goalsData[pos]
    }

    fun addGoal(goal: Goal) {
        goalsData.toMutableList().add(goal)
        notifyDataSetChanged()
    }

    fun deleteGoal(goal: Goal, selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = goalsData[removedPosition]

        goalsData.removeAt(removedPosition)
        projectViewModel.deleteGoal(goal, selectedProject)
        notifyItemRemoved(removedPosition)


        Snackbar.make(viewHolder.itemView, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addGoal(goal, selectedProject)
            goalsData.toMutableList().add(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}