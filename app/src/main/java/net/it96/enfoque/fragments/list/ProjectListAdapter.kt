package net.it96.enfoque.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.project_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Project

class ProjectListAdapter(
    private val context: Context,
    private var projectList: List<Project>,
    private val itemClickListener : OnProjectClickListener
) : RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    //    private var projectList = mutableListOf<Project>()
    interface OnProjectClickListener{
        fun onProjectClick(project: Project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.project_row, parent, false)
        return ProjectViewHolder(view)
//        return ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.project_row, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectList[position]
//        holder.itemView.projectId_txt.text = currentItem.id.toString()
//        holder.itemView.projectName_txt.text = currentItem.name
//        holder.itemView.projectResults_txt.text = currentItem.results
//        holder.itemView.projectGoals90_txt.text = currentItem.goals90
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(project: Project) {
            Glide.with(context).load(project.imageUrl).centerCrop().into(itemView.projectImage)
//            itemView.projectId_txt.text = project.id
            itemView.projectName_txt.text = project.name
//            itemView.projectResults_txt.text = project.results
//            itemView.projectGoals90_txt.text = project.goals90
            itemView.setOnClickListener { itemClickListener.onProjectClick(project) }
        }
    }

    fun setListData(data: MutableList<Project>) {
        projectList = data
    }

}