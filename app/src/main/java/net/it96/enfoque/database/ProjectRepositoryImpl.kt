package net.it96.enfoque.database

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.it96.enfoque.vo.Resource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Clase para hacer los queries a la base de datos
 */
class ProjectRepositoryImpl : ProjectRepository {

    private val TAG = "ProjectRepositoryImpl"

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

//    @ExperimentalCoroutinesApi
//    suspend fun getProjectData(document: String) : Flow<Resource<Project>> = callbackFlow {
//        val projectDocument = firestore.collection("projects").document(document)
//        val suscription = projectDocument.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
//            if(documentSnapshot!!.exists()) {
//                val project = documentSnapshot.toObject(Project::class.java)
//                offer(Resource.Success(project!!))
//            } else {
//                channel.close(firebaseFirestoreException?.cause)
//            }
//        }
//
//        awaitClose { suscription.remove() }
//    }

    @ExperimentalCoroutinesApi
    override suspend fun getProjectList(): Flow<Resource<List<Project>>> = callbackFlow {
        val projectListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects")
        val subscription = projectListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                val projectList = collectionSnapshot.toObjects(Project::class.java)
                offer(Resource.Success(projectList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getGoalsList(selectedProject: String): Flow<Resource<List<Goal>>> = callbackFlow {
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Goals").orderBy("id", Query.Direction.DESCENDING)
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                var goal : Goal? = collectionSnapshot.documents[0].toObject(Goal::class.java)
                Log.i(TAG, "MZP goal: ${goal}")
                goal = collectionSnapshot.documents[2].toObject(Goal::class.java)
                Log.i(TAG, "MZP  goal: ${goal}")
                val goalsList = collectionSnapshot.toObjects(Goal::class.java)
                goalsList.sortBy { goal?.id }
                offer(Resource.Success(goalsList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getKeyResultsList(selectedProject: String): Flow<Resource<List<KeyResult>>> = callbackFlow {
        val keyResultListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Results")
        val subscription = keyResultListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                val keyResultsList = collectionSnapshot.toObjects(KeyResult::class.java)
                offer(Resource.Success(keyResultsList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getTasksList(selectedProject: String): Flow<Resource<List<Task>>> = callbackFlow {
        val tasksListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Tasks").orderBy("id", Query.Direction.DESCENDING)
        val subscription = tasksListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                val tasksList = collectionSnapshot.toObjects(Task::class.java)
                offer(Resource.Success(tasksList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalCoroutinesApi
    override suspend fun getTodayTasksList(): Flow<Resource<List<Task>>> = callbackFlow {
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val today = date.format(formatter).substring(0, 12)
        Log.i(TAG, "MZP today's date: $today")
//        val todayTasksListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document("Focus App").collection("Tasks").whereEqualTo("date", today)
        val todayTasksListCollection = firestore.collectionGroup("Tasks").whereEqualTo("date", today).whereEqualTo("userEmail",user!!.email!!.toString())
        val subscription = todayTasksListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            try {
                if(!collectionSnapshot!!.isEmpty) {
                    val tasksList = collectionSnapshot.toObjects(Task::class.java)
                    offer(Resource.Success(tasksList))
                }
                if(firestoreError != null) {
                    channel.close(firestoreError.cause)
                }
            } catch (e: Exception){
                Log.i(TAG, "MZP An error occurred: $firestoreError -- ${e.printStackTrace()}")
            }

        }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getNotesList(selectedProject: String): Flow<Resource<List<Note>>> = callbackFlow {
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Notes")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                val notesList = collectionSnapshot.toObjects(Note::class.java)
                offer(Resource.Success(notesList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    // Receives the data and stores it on Firestore
    /*
    * Add New Data
    * */
    override suspend fun addProject(project: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(project.name)
            .set(project)
            .addOnSuccessListener {
                Log.i(TAG,"***MZP*** Document(Project) saved")
            }
            .addOnFailureListener {
                Log.i(TAG,"***MZP*** Saved failed")
            }
    }

    override suspend fun addGoal(goal: Goal) {
        Log.i(TAG, "MZP goal: $goal")
//        val goalToAdd = hashMapOf(
//            "id" to goal.id,
//            "description" to goal.description,
//            "projectName" to goal.projectName
//        )
        firestore.collection("users")

            .document(user!!.email!!)
            .collection("Projects")
            .document(goal.projectName)
            .collection("Goals")
            .document(goal.id)
//            .set(goalToAdd)
            .set(goal)
            .addOnSuccessListener {
                Log.i(TAG,"***MZP*** Goal saved")
            }
            .addOnFailureListener {
                Log.e(TAG,"***MZP*** Saved failed")
            }
    }

    override suspend fun addKeyResult(keyResult: KeyResult) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(keyResult.projectName)
            .collection("Results")
            .document(keyResult.id)
            .set(keyResult)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Key Result saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    override suspend fun addTask(task: Task) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(task.projectName)
            .collection("Tasks")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Task saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    override suspend fun addNote(note: Note) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(note.projectName)
            .collection("Notes")
            .document(note.id)
            .set(note)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Note saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    /*
    * Edit Data
    * */

    override suspend fun editGoal(goal: Goal) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(goal.projectName)
            .collection("Goals")
            .document(goal.id)
            .set(goal)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Goal saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    override suspend fun editKeyResult(keyResult: KeyResult) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(keyResult.projectName)
            .collection("Results")
            .document(keyResult.id)
            .set(keyResult)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Key Result saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    override suspend fun editTask(task: Task) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(task.projectName)
            .collection("Tasks")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Task saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    override suspend fun editNote(note: Note) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(note.projectName)
            .collection("Notes")
            .document(note.id)
            .set(note)
            .addOnSuccessListener {
                Log.i(TAG, "***MZP*** Note saved")
            }
            .addOnFailureListener {
                Log.e(TAG, "***MZP*** Saved failed")
            }
    }

    /*
    * Delete Data
    * */

    override suspend fun deleteProject(selectedProject: Project){
        // Get the Project document to delete
        firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).delete().await()
    }

    override suspend fun deleteGoal(goal: Goal){
        // Get the collection that include the documents that match with the query
        val goalListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(goal.projectName).collection("Goals").whereEqualTo("description",goal.description).get().await()

        goalListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(goal.projectName).collection("Goals").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteKeyResult(keyResult: KeyResult){
        // Get the collection that include the documents that match with the query
        val keyResultListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(keyResult.projectName).collection("Results").whereEqualTo("description",keyResult.description).get().await()

        keyResultListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(keyResult.projectName).collection("Results").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteTask(task: Task) {
        // Get the collection that include the documents that match with the query
        val noteListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(task.projectName).collection("Tasks").whereEqualTo("description",task.description).get().await()

        noteListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(task.projectName).collection("Tasks").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteNote(note: Note) {
        // Get the collection that include the documents that match with the query
        val noteListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(note.projectName).collection("Notes").whereEqualTo("description",note.description).get().await()

        noteListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(note.projectName).collection("Notes").document(doc.id).delete().await()
        }
    }
}