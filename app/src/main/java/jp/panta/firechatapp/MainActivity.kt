package jp.panta.firechatapp

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.panta.firechatapp.databinding.ActivityMainBinding
import jp.panta.firechatapp.models.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SettableTitle {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val bottomNavFragments = setOf(
            R.id.navigation_home,
            R.id.navigation_account
        )
        val appBarConfiguration = AppBarConfiguration(
            bottomNavFragments
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.visibility = if(bottomNavFragments.contains(destination.id)){
                View.VISIBLE
            }else{
                View.GONE
            }
        }

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        /*lifecycleScope.launchWhenResumed {
            authViewModel.state.collect {
                when(it) {
                    is AuthState.Unauthorized -> {
                        val providers = arrayListOf(
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                        val ui = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build()
                        signInLauncher.launch(ui)
                    }
                    is AuthState.Loading -> {
                        Log.d("HomeFragment", "Loading")
                    }
                    is AuthState.Authorized -> {
                        Log.d("HomeFragment", "Authorized")
                    }
                }
            }
        }*/

    }

    /*private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { _ ->

    }*/

    override fun setTitle(text: String) {
        supportActionBar?.title = text
    }
}