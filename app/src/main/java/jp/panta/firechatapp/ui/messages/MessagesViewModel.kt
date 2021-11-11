package jp.panta.firechatapp.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.shareIn

@ExperimentalCoroutinesApi
class MessagesViewModel(
    roomId: String
) : ViewModel(){

    class Factory(private val roomId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessagesViewModel(roomId) as T
        }
    }

    val messages = Firebase.firestore.collection("rooms").document(roomId)
        .collection("messages")
        .asFlow().map {
            Log.d("MessagesViewModel", "${it.documents.map { it.data }}")
            it.toObjects(Message::class.java)
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


}