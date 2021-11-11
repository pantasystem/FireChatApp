package jp.panta.firechatapp.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.datetime.Instant
import java.util.*


data class Message (
    @DocumentId val id: String,
    val user: User,
    val userId: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    fun toMapForCreate(): Map<String, Any?> {
        return mapOf(
            "user_id" to userId,
            "created_at" to Timestamp(Date(createdAt.epochSeconds)),
            "updated_at" to Timestamp(Date(updatedAt.epochSeconds))
        )
    }
}