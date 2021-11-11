package jp.panta.firechatapp.models

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String,
    val username: String,
    val profileUrl: String?
) {
    constructor() : this("", "", null)
}