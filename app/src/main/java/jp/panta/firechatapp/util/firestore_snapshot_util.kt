package jp.panta.firechatapp.util

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@ExperimentalCoroutinesApi
fun CollectionReference.asFlow(): Flow<QuerySnapshot> {

    return channelFlow {
        val l = this@asFlow.addSnapshotListener { value, error ->
            Log.d("asFlow", "value:$value, error:$error")
            if(error != null) {
                throw error
            }
            if(value != null) {
                this.trySend(value)
            }

        }
        awaitClose {
            l.remove()
        }
    }

}

@ExperimentalCoroutinesApi
fun DocumentReference.asFlow(): Flow<DocumentSnapshot?> {
    return channelFlow {
        val l = this@asFlow.addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            this.trySend(value)
        }
        awaitClose {
            l.remove()
        }
    }
}


@ExperimentalCoroutinesApi
fun Query.asFlow(): Flow<QuerySnapshot?> {

    return channelFlow {

        val l = this@asFlow.addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            if(value != null) {
                this.trySend(value)
            }
        }
        awaitClose {
            l.remove()
        }
    }
}

suspend fun<T> Task<T>.asSuspend() = suspendCoroutine<T> { continuation ->
    addOnSuccessListener {
        continuation.resume(it)
    }
    addOnFailureListener {
        throw it
    }
}

