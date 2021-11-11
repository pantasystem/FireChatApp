package jp.panta.firechatapp.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.panta.firechatapp.models.Message
import jp.panta.firechatapp.models.MessageView
import jp.panta.firechatapp.models.Room
import jp.panta.firechatapp.models.User
import jp.panta.firechatapp.util.asFlow
import jp.panta.firechatapp.util.asSuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import java.util.*

@ExperimentalCoroutinesApi
class MessagesViewModel(
    private val roomId: String
) : ViewModel(){

    class Factory(private val roomId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessagesViewModel(roomId) as T
        }
    }

    val messages = Firebase.firestore.collection("rooms").document(roomId)
        .collection("messages")
        .orderBy("createdAt")
        .asFlow().mapNotNull {
            Log.d("MessagesViewModel", "${it?.documents?.map { it.data }}")
            it?.toObjects(Message::class.java)
        }.map {
            it.map {  msg ->
                MessageView(
                    id = msg.id,
                    createdAt = msg.createdAt,
                    updatedAt = msg.updatedAt,
                    text = msg.text,
                    userId = msg.userId,
                    user = msg.userRef?.get()?.asSuspend()?.toObject(User::class.java)
                )
            }
        }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    val room = Firebase.firestore.collection("rooms").document(roomId)
        .asFlow()
        .map {
            it?.toObject(Room::class.java)
        }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)

    suspend fun create(text: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Firebase.firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .add(Message(
                id = "",
                userId = currentUser!!.uid,
                text = text,
                createdAt = Date(),
                updatedAt = Date(),
                userRef = Firebase.firestore.collection("users").document(currentUser.uid)
            )).asSuspend()

    }


}