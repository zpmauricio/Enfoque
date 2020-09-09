package net.it96.enfoque.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.project_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.data.Project

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var projectList = emptyList<Project>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.project_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.itemView.projectId_txt.text = currentItem.id.toString()
        holder.itemView.projectName_txt.text = currentItem.name
        holder.itemView.projectResults_txt.text = currentItem.results
        holder.itemView.projectGoals90_txt.text = currentItem.goals90
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setData(project: List<Project>) {
        this.projectList = project
        notifyDataSetChanged()
    }
}