package net.it96.enfoque.database

import kotlinx.coroutines.flow.Flow
import net.it96.enfoque.vo.Resource

interface ProjectRepository {
    suspend fun getProjectList() : Flow<Resource<List<Project>>>
    suspend fun getGoalsList(selectedProject: String) : Flow<Resource<List<Goal>>>
    suspend fun getKeyResultsList(selectedProject: String) : Flow<Resource<List<KeyResult>>>
    suspend fun getTasksList(selectedProject: String) : Flow<Resource<List<Task>>>
    suspend fun getTodayTasksList() : Flow<Resource<List<Task>>>
    suspend fun getNotesList(selectedProject: String) : Flow<Resource<List<Note>>>

    suspend fun addProject(project: Project)
    suspend fun addGoal(goal: Goal)
    suspend fun addKeyResult(keyResult: KeyResult)
    suspend fun addTask(task: Task)
    suspend fun addNote(note: Note)

    suspend fun editGoal(goal: Goal)
    suspend fun editKeyResult(keyResult: KeyResult)
    suspend fun editTask(task: Task)
    suspend fun editNote(note: Note)

    suspend fun deleteProject(selectedProject: Project)
    suspend fun deleteGoal(goal: Goal)
    suspend fun deleteKeyResult(keyResult: KeyResult)
    suspend fun deleteTask(task: Task)
    suspend fun deleteNote(note: Note)
}