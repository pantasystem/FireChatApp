package jp.panta.firechatapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import jp.panta.firechatapp.AuthState
import jp.panta.firechatapp.AuthViewModel
import jp.panta.firechatapp.databinding.FragmentAccountBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AccountFragment : Fragment(){

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        val accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        authViewModel.state.onEach {
            accountViewModel.setState(it)
        }.launchIn(lifecycleScope)

        lifecycleScope.launchWhenResumed {
            accountViewModel.visibleLogoutButton.collect {
                binding.logoutButton.visibility = if(it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launchWhenResumed {
            accountViewModel.visibleLoginButton.collect {
                binding.loginButton.visibility = if(it) View.VISIBLE else View.GONE
            }
        }

        binding.logoutButton.setOnClickListener {
            authViewModel.logout()
        }

        binding.loginButton.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
            val ui = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(ui)
        }


    }
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->

    }
}