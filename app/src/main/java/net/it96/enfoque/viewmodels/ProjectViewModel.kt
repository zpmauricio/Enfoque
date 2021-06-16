package net.it96.enfoque.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.it96.enfoque.database.*
import net.it96.enfoque.vo.Resource

/**
 * Clase para observar cambios en los datos
 */
class ProjectViewModel(private val projectRepository : ProjectRepository, private val param : String) : ViewModel() {

    private val TAG = "ProjectViewModel"

    val getProjectList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getProjectList().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Log.e(TAG,"***MZP*** ERROR GETTING PROJECT LIST")
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
            Log.e(TAG, "***MZP*** ERROR GETTING GOALS LIST: $e")
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
            Log.e(TAG, "***MZP*** ERROR GETTING KEY RESULTS LIST: $e")
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
            Log.e(TAG, "***MZP*** ERROR GETTING TASKS LIST: $e")
        }
    }

    val getTodayTasksList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            projectRepository.getTodayTasksList().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Log.e(TAG, "***MZP*** ERROR GETTING TASKS LIST: $e")
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
            Log.e(TAG, "***MZP*** ERROR GETTING NOTES LIST: $e")
        }
    }

    /* Involve the Dispatchers to use Coroutines and send the data to the Repository */
    /*
        Add Data
    */
    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.addProject(project)
        }
    }

    fun addGoal(goal: Goal) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "MZP goal: $goal")
            projectRepository.addGoal(goal)
        }
    }

    fun addKeyResult(keyResult: KeyResult) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.addKeyResult(keyResult)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.addTask(task)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.addNote(note)
        }
    }

    /*
        Edit Data
    */

    fun editGoal(goal: Goal) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.editGoal(goal)
        }
    }

    fun editKeyResult(keyResult: KeyResult) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.editKeyResult(keyResult)
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.editTask(task)
        }
    }

    fun editNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.editNote(note)
        }
    }

    /*
        Delete Data
    */
    fun deleteProject(project: Project){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteProject(project)
        }
    }

    fun deleteGoal(goal: Goal){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteGoal(goal)
        }
    }

    fun deleteKeyResult(keyResult: KeyResult){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteKeyResult(keyResult)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteTask(task)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteNote(note)
        }
    }
}