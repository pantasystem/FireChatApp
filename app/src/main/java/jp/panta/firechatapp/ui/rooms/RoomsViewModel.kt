package jp.panta.firechatapp.ui.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.panta.firechatapp.models.Room
import jp.panta.firechatapp.util.asFlow
import jp.panta.firechatapp.util.asSuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class RoomsViewModel : ViewModel(){



    private val db: FirebaseFirestore
        get() = Firebase.firestore


    @ExperimentalCoroutinesApi
    private val rooms = db.collection("rooms")
        .asFlow()
        .shareIn(viewModelScope, started = SharingStarted.Lazily)


    suspend fun create(room: Room) {
        db.collection("rooms")
            .add(room)
            .asSuspend()
    }


}