package jp.panta.firechatapp.ui.account

import androidx.lifecycle.ViewModel
import jp.panta.firechatapp.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class AccountViewModel : ViewModel() {

    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val state: StateFlow<AuthState> = _state

    val visibleLogoutButton = state.map {
        it is AuthState.Authorized
    }

    val visibleLoginButton = state.map {
        it is AuthState.Unauthorized
    }

    fun setState(state: AuthState) {
        _state.value = state
    }


}