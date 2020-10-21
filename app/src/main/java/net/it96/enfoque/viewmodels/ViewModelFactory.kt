package net.it96.enfoque.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.it96.enfoque.database.ProjectRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val projectRepository : ProjectRepository, private val param : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(ProjectRepository::class.java).newInstance(projectRepository, param)
        return ProjectViewModel(projectRepository, param) as T
    }
}