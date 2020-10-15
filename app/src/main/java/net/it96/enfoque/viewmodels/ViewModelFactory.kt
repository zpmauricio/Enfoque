package net.it96.enfoque.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.it96.enfoque.database.ProjectRepository

class ViewModelFactory(private val projectRepository : ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProjectRepository::class.java).newInstance(projectRepository)
    }
}