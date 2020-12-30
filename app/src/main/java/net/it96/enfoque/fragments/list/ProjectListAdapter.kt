package net.it96.enfoque.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import net.it96.enfoque.database.Project
import net.it96.enfoque.databinding.ProjectRowBinding
import net.it96.enfoque.viewmodels.ProjectViewModel

class ProjectListAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel,
    private val itemClickListener : OnProjectClickListener?
) : RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Project? = null

    private var projectData = mutableListOf<Project>()

    //    private var projectList = mutableListOf<Project>()
    interface OnProjectClickListener{
        fun onProjectClick(project: Project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.project_row, parent, false)
        val itemBinding = ProjectRowBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = ProjectViewHolder(itemBinding)
        return holder
//        return view
//        return ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.project_row, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectData[position]
//        holder.itemView.projectId_txt.text = currentItem.id.toString()
//        holder.itemView.projectName_txt.text = currentItem.name
//        holder.itemView.projectResults_txt.text = currentItem.results
//        holder.itemView.projectGoals90_txt.text = currentItem.goals90
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = projectData.size

//    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    inner class ProjectViewHolder(val binding: ProjectRowBinding) : RecyclerView.ViewHolder(binding.root) {
         fun bindView(project: Project) = with(binding) {
            Glide.with(context).load(project.imageUrl).centerCrop().into( projectImage)
//            itemView.projectId_txt.text = project.id
            projectNameTxt.text = project.name
//            itemView.projectResults_txt.text = project.results
//            itemView.projectGoals90_txt.text = project.goals90
            binding.root.setOnClickListener { itemClickListener?.onProjectClick(project) }
        }
    }

    fun setListData(data: MutableList<Project>) {
        projectData = data
    }

    fun getObject(pos: Int) : Project {
        return projectData[pos]
    }

    fun addProject(position: Int, project: Project) {
        projectData.add(position, project)
    }

    fun deleteProject(selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = projectData[removedPosition]

        projectData.removeAt(removedPosition)
        projectViewModel.deleteProject(selectedProject)
        notifyItemRemoved(removedPosition)

        Snackbar.make(viewHolder.itemView, "${removedItem!!.name} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addProject(selectedProject)
            addProject(removedPosition, removedItem!!)
            notifyDataSetChanged()
        }.show()
    }

}