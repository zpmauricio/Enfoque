package net.it96.enfoque.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import net.it96.enfoque.vo.Resource
import timber.log.Timber

/**
 * Clase para hacer los queries a la base de datos
 */
class ProjectRepositoryImpl : ProjectRepository {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

//    fun getAllProjectsData(): LiveData<MutableList<Project>> {
//        val mutableData = MutableLiveData<MutableList<Project>>()
//        firestore.collection("projects").addSnapshotListener { snapshot, firestoreError ->
//            if (firestoreError != null) {
//                Timber.e("***MZP*** Error en firestore: + ${firestoreError.message}")
//                return@addSnapshotListener
//            }
//            val listData = mutableListOf<Project>()
//            for (document in snapshot!!) {
//                val project = document.toObject(Project::class.java)
//                listData.add(project)
//            }
//            mutableData.value = listData
//        }
//        return mutableData
//    }

//    @ExperimentalCoroutinesApi
//    fun getAllProjectsData() : Flow<Resource<MutableList<Project>>> = callbackFlow {
//        val projectListCollection = firestore.collection("projects")
//        val suscription = projectListCollection.addSnapshotListener { snapshot, firestoreError ->
//            if(firestoreError != null) {
//                channel.close(firestoreError?.cause)
//            }
//
//            val projectList = snapshot
//        }
//    }

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
//    suspend fun getProjectData(document: String): Flow<Resource<Project>> {
//        val projectData = firestore.collection("projects").document(document).get().await()
//        val selectedProject = projectData.toObject(Project::class.java)
//
//        return Resource.Success(selectedProject!!)
//    }

    // Receives the data and stores it on Firestore
    fun save(project: Project) {
        firestore.collection("projects")
            .document("user2")
            .set(project)
            .addOnSuccessListener {
                Timber.i("Document(Project) saved")
            }
            .addOnFailureListener {
                Timber.i("Saved failed")
            }
    }

    fun setUserData(name: String, email: String, password: String) {

        val userHashMap = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        firestore.collection("users")
            .add(userHashMap)
            .addOnSuccessListener {
                Timber.i("User saved. ID = %s", it.id)
            }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getProjectList(): Flow<Resource<List<Project>>> = callbackFlow {
        Timber.i("***MZP*** getProjectList()")
        val projectListCollection = firestore.collection("users").document("mauzav").collection("Projects")
        val suscription = projectListCollection.addSnapshotListener { collectionSnapshot, firestoreError ->
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

        awaitClose { suscription.remove() }
    }
}