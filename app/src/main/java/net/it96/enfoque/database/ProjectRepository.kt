package net.it96.enfoque.database

import kotlinx.coroutines.flow.Flow
import net.it96.enfoque.vo.Resource

interface ProjectRepository {
    suspend fun getProjectList() : Flow<Resource<List<Project>>>
    suspend fun getGoalsList(selectedProject: String) : Flow<Resource<List<Goal>>>
    suspend fun getKeyResultsList(selectedProject: String) : Flow<Resource<List<KeyResult>>>
    suspend fun getTasksList(selectedProject: String) : Flow<Resource<List<Task>>>
    suspend fun getNotesList(selectedProject: String) : Flow<Resource<List<Note>>>

    suspend fun saveProject(project: Project)
    suspend fun saveGoal(goal: Goal, selectedProject: Project)
}