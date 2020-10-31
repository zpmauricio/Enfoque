package net.it96.enfoque.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.it96.enfoque.vo.Resource
import timber.log.Timber

/**
 * Clase para hacer los queries a la base de datos
 */
class ProjectRepositoryImpl : ProjectRepository {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    @ExperimentalCoroutinesApi
    suspend fun getProjectData(document: String) : Flow<Resource<Project>> = callbackFlow {
        val projectDocument = firestore.collection("projects").document(document)
        val suscription = projectDocument.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if(documentSnapshot!!.exists()) {
                val project = documentSnapshot.toObject(Project::class.java)
                offer(Resource.Success(project!!))
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }
        }

        awaitClose { suscription.remove() }
    }

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
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Goals")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                val goalsList = collectionSnapshot.toObjects(Goal::class.java)
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
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Tasks")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
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
    * Save New Data
    * */
    override suspend fun saveProject(project: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(project.name)
            .set(project)
            .addOnSuccessListener {
                Timber.i("***MZP*** Document(Project) saved")
            }
            .addOnFailureListener {
                Timber.i("***MZP*** Saved failed")
            }
    }

    override suspend fun saveGoal(goal: Goal, selectedProject: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(selectedProject.name)
            .collection("Goals")
            .add(goal)
            .addOnSuccessListener {
                Timber.i("***MZP*** Goal saved")
            }
            .addOnFailureListener {
                Timber.e("***MZP*** Saved failed")
            }
    }

    override suspend fun saveKeyResult(keyResult: KeyResult, selectedProject: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(selectedProject.name)
            .collection("Results")
            .add(keyResult)
            .addOnSuccessListener {
                Timber.i("***MZP*** Key Result saved")
            }
            .addOnFailureListener {
                Timber.e("***MZP*** Saved failed")
            }
    }

    override suspend fun saveTask(task: Task, selectedProject: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(selectedProject.name)
            .collection("Tasks")
            .add(task)
            .addOnSuccessListener {
                Timber.i("***MZP*** Note saved")
            }
            .addOnFailureListener {
                Timber.e("***MZP*** Saved failed")
            }
    }

    override suspend fun saveNote(note: Note, selectedProject: Project) {
        firestore.collection("users")
            .document(user!!.email!!)
            .collection("Projects")
            .document(selectedProject.name)
            .collection("Notes")
            .add(note)
            .addOnSuccessListener {
                Timber.i("***MZP*** Note saved")
            }
            .addOnFailureListener {
                Timber.e("***MZP*** Saved failed")
            }
    }

    /*
    * Delete Data
    * */

    override suspend fun deleteGoal(goal: Goal, selectedProject: Project){
        // Get the collection that include the documents that match with the query
        val goalListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).collection("Goals").whereEqualTo("description",goal.description).get().await()

        goalListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(selectedProject.name).collection("Goals").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteKeyResult(keyResult: KeyResult, selectedProject: Project){
        // Get the collection that include the documents that match with the query
        val keyResultListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).collection("Results").whereEqualTo("description",keyResult.description).get().await()

        keyResultListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(selectedProject.name).collection("Results").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteTask(task: Task, selectedProject: Project) {
        // Get the collection that include the documents that match with the query
        val noteListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).collection("Tasks").whereEqualTo("description",task.description).get().await()

        noteListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(selectedProject.name).collection("Tasks").document(doc.id).delete().await()
        }
    }

    override suspend fun deleteNote(note: Note, selectedProject: Project) {
        // Get the collection that include the documents that match with the query
        val noteListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).collection("Notes").whereEqualTo("description",note.description).get().await()

        noteListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(selectedProject.name).collection("Notes").document(doc.id).delete().await()
        }
    }
}