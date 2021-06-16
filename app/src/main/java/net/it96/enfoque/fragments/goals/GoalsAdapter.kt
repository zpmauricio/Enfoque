package net.it96.enfoque.fragments.goals

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.it96.enfoque.database.Goal
import net.it96.enfoque.databinding.FragmentProjectDetailBinding
import net.it96.enfoque.databinding.GoalRowBinding
import net.it96.enfoque.viewmodels.ProjectViewModel

class GoalsAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {

    private val TAG = "GoalsAdapter"
    private var removedPosition : Int = 0
    private var removedItem : Goal? = null
    var topId : Int = 0

    private var goalsData = mutableListOf<Goal>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAdapter.GoalsViewHolder {
        val itemBinding = GoalRowBinding.inflate(LayoutInflater.from(context), parent, false)

        return GoalsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GoalsAdapter.GoalsViewHolder, position: Int) {
        val currentItem = goalsData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = goalsData.size

    inner class GoalsViewHolder(val binding: GoalRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(goal: Goal) {
            Log.i(TAG, "MZP goal: $goal")
            Log.i(TAG, "MZP goal.id: ${goal.id}")
            binding.txtGoal.text = goal.description
            binding.etxtEditGoal.setText(goal.description)
            if(goal.id.toInt() > topId) topId = goal.id.toInt()

            binding.cvRowGoals.setOnClickListener {
                it.visibility = View.GONE
                binding.cvRowEditGoals.visibility = View.VISIBLE
            }

            binding.ibSaveEditGoal.setOnClickListener {
                goal.description = binding.etxtEditGoal.text.toString()
                projectViewModel.editGoal(goal)
                binding.cvRowEditGoals.visibility = View.GONE
                binding.cvRowGoals.visibility = View.VISIBLE
            }
        }
    }

    fun setListData(data: MutableList<Goal>) {
        goalsData = data
    }

    fun getObject(pos: Int) : Goal {
        return goalsData[pos]
    }

    fun addGoal(goal: Goal) {
        goalsData.add(goal)
    }

    fun deleteGoal(goal: Goal, viewHolder: RecyclerView.ViewHolder, binding: FragmentProjectDetailBinding) {
        removedPosition = viewHolder.adapterPosition
        removedItem = goalsData[removedPosition]

        goalsData.removeAt(removedPosition)
        projectViewModel.deleteGoal(goal)
        notifyItemRemoved(removedPosition)

        Snackbar.make(binding.clDetailMainLayout, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addGoal(goal)
            addGoal(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}