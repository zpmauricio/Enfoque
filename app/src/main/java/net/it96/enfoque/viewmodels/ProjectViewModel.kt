package net.it96.enfoque.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.it96.enfoque.database.Project
import net.it96.enfoque.database.ProjectRepository
import net.it96.enfoque.database.ProjectRepositoryImpl
import timber.log.Timber

/**
 * Clase para observar cambios en los datos
 */
class ProjectViewModel(private val projectRepository : ProjectRepository) : ViewModel() {

    private val projectRepositoryImpl = ProjectRepositoryImpl()

    val getProjectList = liveData(Dispatchers.IO) {
//        emit(Resource.Loading())
        try{
            Timber.i("***MZP*** GET PROJECT LIST")
            projectRepository.getProjectList().collect {
                emit(it)
            }
            Timber.i("***MZP*** PROJECT LIST WAS OBTAINED")
        } catch (e: Exception) {
//            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING PROJECT LIST")
        }
    }

    // Involve the Dispatchers to use Coroutines and send the data to the Repository
    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepositoryImpl.save(project)
        }
    }

}