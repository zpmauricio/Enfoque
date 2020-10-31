package net.it96.enfoque.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.it96.enfoque.database.*
import net.it96.enfoque.vo.Resource
import timber.log.Timber

/**
 * Clase para observar cambios en los datos
 */
class ProjectViewModel(private val projectRepository : ProjectRepository, private val param : String) : ViewModel() {

    private val projectRepositoryImpl = ProjectRepositoryImpl()

    val getProjectList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getProjectList().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING PROJECT LIST")
        }
    }

    val getGoalsList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getGoalsList(param).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING GOALS LIST: $e")
        }
    }

    val getKeyResultsList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getKeyResultsList(param).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING KEY RESULTS LIST: $e")
        }
    }

    val getTasksList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getTasksList(param).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING TASKS LIST: $e")
        }
    }

    val getNotesList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getNotesList(param).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Timber.e("***MZP*** ERROR GETTING NOTES LIST: $e")
        }
    }

    /* Involve the Dispatchers to use Coroutines and send the data to the Repository */
    /*
        Add Data
    */
    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveProject(project)
        }
    }

    fun addGoal(goal: Goal, selectedProject: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveGoal(goal, selectedProject)
        }
    }

    fun addKeyResult(keyResult: KeyResult, selectedProject: Project) {
        Timber.i("***MZP***")
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveKeyResult(keyResult, selectedProject)
        }
    }

    fun addTask(task: Task, selectedProject: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveTask(task, selectedProject)
        }
    }

    fun addNote(note: Note, selectedProject: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveNote(note, selectedProject)
        }
    }

    /*
        Delete Data
    */
    fun deleteGoal(goal: Goal, selectedProject: Project){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteGoal(goal, selectedProject)
        }
    }

    fun deleteKeyResult(keyResult: KeyResult, selectedProject: Project){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteKeyResult(keyResult, selectedProject)
        }
    }

    fun deleteTask(task: Task, selectedProject: Project){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteTask(task, selectedProject)
        }
    }

    fun deleteNote(note: Note, selectedProject: Project){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteNote(note, selectedProject)
        }
    }
}