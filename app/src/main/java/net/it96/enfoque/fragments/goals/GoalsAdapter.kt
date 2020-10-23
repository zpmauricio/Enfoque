package net.it96.enfoque.fragments.goals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.goal_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Goal
import timber.log.Timber

class GoalsAdapter(
    private val context: Context,
    private var goalList: List<Goal>,
) : RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAdapter.GoalsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_row, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalsAdapter.GoalsViewHolder, position: Int) {
        val currentItem = goalList[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    inner class GoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(goal: Goal) {
            Timber.i("***MZP*** goal.description: ${goal.description}")
            itemView.txt_goal.text = goal.description
        }
    }

    fun setListData(data: MutableList<Goal>) {
        goalList = data
    }

}