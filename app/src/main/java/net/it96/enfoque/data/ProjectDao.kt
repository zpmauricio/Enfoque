package net.it96.enfoque.data
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//@Dao
//interface ProjectDao {
//
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    fun addProject(project: Project)
//
//    @Query("SELECT * FROM project_table ORDER BY id ASC")
//    fun readAllData(): LiveData<List<Project>>
//}