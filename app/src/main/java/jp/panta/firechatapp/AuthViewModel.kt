package jp.panta.firechatapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.panta.firechatapp.models.User
import jp.panta.firechatapp.util.asSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface AuthState {
    object Loading : AuthState
    object Unauthorized : AuthState
    data class Authorized(
        val user: FirebaseUser
    ) : AuthState
}
class AuthViewModel : ViewModel(){

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState>
        get() = _state

    private val listener: (FirebaseAuth)-> Unit = {
        _state.value = if (it.currentUser == null) {
            AuthState.Unauthorized
        } else {
            AuthState.Authorized(it.currentUser!!)
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(listener)

        viewModelScope.launch(Dispatchers.IO) {
            state.mapNotNull {
                it as? AuthState.Authorized
            }.collect {
                runCatching {
                    val refTask = Firebase.firestore.collection("users")
                        .document(it.user.uid)
                        .get().asSuspend()

                    if(!refTask.exists()) {
                        Firebase.firestore.collection("users")
                            .document(it.user.uid)
                            .set(User("", "", null))
                            .asSuspend()
                    }
                }.onFailure {
                    Log.e("AVM", "failure", it)
                }

            }

        }
    }



    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(listener)
    }
}