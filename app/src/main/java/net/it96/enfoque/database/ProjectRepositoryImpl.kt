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
        Timber.i("***MZP*** getProjectList()")
        val projectListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects")
        val subscription = projectListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                Timber.i("***MZP*** collectionSnapshot: %s", collectionSnapshot.documents)
                val projectList = collectionSnapshot.toObjects(Project::class.java)
                Timber.i("***MZP*** projectList: %s", projectList.toString())
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
        Timber.i("***MZP*** getGoalsList()")
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Goals")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                Timber.i("***MZP*** collectionSnapshot: %s", collectionSnapshot.documents)
                val goalsList = collectionSnapshot.toObjects(Goal::class.java)
                Timber.i("***MZP*** goalsList: %s", goalsList.toString())
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
        Timber.i("***MZP*** getKeyResultsList()")
        val keyResultListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Results")
        val subscription = keyResultListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                Timber.i("***MZP*** collectionSnapshot: %s", collectionSnapshot.documents)
                val keyResultsList = collectionSnapshot.toObjects(KeyResult::class.java)
                Timber.i("***MZP*** keyResultsList: %s", keyResultsList.toString())
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
        Timber.i("***MZP*** getKeyResultsList()")
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Tasks")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                Timber.i("***MZP*** collectionSnapshot: %s", collectionSnapshot.documents)
                val tasksList = collectionSnapshot.toObjects(Task::class.java)
                Timber.i("***MZP*** tasksList: %s", tasksList.toString())
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
        Timber.i("***MZP*** getKeyResultsList()")
        val goalsListCollection = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject).collection("Notes")
        val subscription = goalsListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
            if(!collectionSnapshot!!.isEmpty) {
                Timber.i("***MZP*** collectionSnapshot: %s", collectionSnapshot.documents)
                val notesList = collectionSnapshot.toObjects(Note::class.java)
                Timber.i("***MZP*** notesList: %s", notesList.toString())
                offer(Resource.Success(notesList))
            }
            if(firestoreError != null) {
                channel.close(firestoreError.cause)
            }
        }

        awaitClose { subscription.remove() }
    }

    // Receives the data and stores it on Firestore
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

    // Receives the data and stores it on Firestore
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

    // Receives the data and stores it on Firestore
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

    override suspend fun deleteKeyResult(keyResult: KeyResult, selectedProject: Project){
        // Get the collection that include the documents that match with the query
        val keyResultListSnapshot = firestore.collection("users").document(user!!.email!!).collection("Projects").document(selectedProject.name).collection("Results").whereEqualTo("description",keyResult.description).get().await()

        keyResultListSnapshot.forEach{doc ->
            firestore.collection("users").document(user.email!!).collection("Projects").document(selectedProject.name).collection("Results").document(doc.id).delete().await()
        }
    }
}