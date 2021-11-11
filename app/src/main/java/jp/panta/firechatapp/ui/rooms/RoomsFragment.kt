package jp.panta.firechatapp.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import jp.panta.firechatapp.R
import jp.panta.firechatapp.databinding.FragmentRoomsBinding

class RoomsFragment : Fragment() {

    private var _binding: FragmentRoomsBinding? = null
    private val binding: FragmentRoomsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRoomButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_roomEditorFragment)
        }

    }
}