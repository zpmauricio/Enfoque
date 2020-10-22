package net.it96.enfoque.fragments.ninetydaygoals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.goal_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.NinetyDayGoal
import timber.log.Timber

class NinetyDayGoalsAdapter(
    private val context: Context,
    private var goalList: List<NinetyDayGoal>,
) : RecyclerView.Adapter<NinetyDayGoalsAdapter.GoalsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NinetyDayGoalsAdapter.GoalsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_row, parent, false)
        return GoalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NinetyDayGoalsAdapter.GoalsViewHolder, position: Int) {
        val currentItem = goalList[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    inner class GoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(goal: NinetyDayGoal) {
            Timber.i("***MZP*** goal.description: ${goal.description}")
            itemView.txt_goal.text = goal.description
        }
    }

    fun setListData(data: MutableList<NinetyDayGoal>) {
        goalList = data
    }

}