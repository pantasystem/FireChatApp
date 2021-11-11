package jp.panta.firechatapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import jp.panta.firechatapp.AuthViewModel
import jp.panta.firechatapp.R
import jp.panta.firechatapp.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment(){

    private var _binding: FragmentSignupBinding? = null
    val binding: FragmentSignupBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.signUpWithGoogleButton.setOnClickListener {
            lifecycleScope.launchWhenCreated {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}