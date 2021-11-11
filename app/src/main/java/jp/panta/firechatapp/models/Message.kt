package jp.panta.firechatapp.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.toObject
import jp.panta.firechatapp.util.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*


data class Message (
    @DocumentId val id: String,
    val userId: String,
    @ServerTimestamp val createdAt: Date,
    @ServerTimestamp val updatedAt: Date,
    val text: String,
    val userRef: DocumentReference?
) {
    constructor() : this("", "", Date(), Date(), "", null)

}

data class MessageView(
    @DocumentId val id: String,
    val userId: String,
    @ServerTimestamp val createdAt: Date,
    @ServerTimestamp val updatedAt: Date,
    val text: String,
    val user: User?
)