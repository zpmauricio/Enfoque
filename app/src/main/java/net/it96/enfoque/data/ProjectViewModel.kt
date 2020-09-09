package net.it96.enfoque.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Project>>
    private val repository: ProjectRepository

    init {
        val projectDao = ProjectDatabase.getDatabase(application).projectDao()
        repository = ProjectRepository(projectDao)
        readAllData = repository.readAllData
    }

    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProject(project)
        }
    }
}