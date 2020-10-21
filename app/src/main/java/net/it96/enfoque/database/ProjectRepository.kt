package net.it96.enfoque.database

import kotlinx.coroutines.flow.Flow
import net.it96.enfoque.vo.Resource

interface ProjectRepository {
    suspend fun getProjectList() : Flow<Resource<List<Project>>>
    suspend fun getGoalsList(selectedProject: String) : Flow<Resource<List<NinetyDayGoal>>>
}