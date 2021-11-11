package jp.panta.firechatapp.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import jp.panta.firechatapp.databinding.FragmentRoomEditorBinding
import jp.panta.firechatapp.models.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomEditorFragment : Fragment(){

    private var _binding: FragmentRoomEditorBinding? = null
    private  val binding: FragmentRoomEditorBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]

        binding.saveButton.setOnClickListener {
            val text = binding.roomTitleEditText.text?.toString() ?: ""
            if(text.isBlank()) {
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val result = runCatching {
                    viewModel.create(Room(id="", name=text))
                }
                withContext(Dispatchers.Main) {
                    result.onSuccess {
                        view.findNavController().navigateUp()
                    }.onFailure {
                        Snackbar.make(binding.root, "作成に失敗しました", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.roomTitleEditText.addTextChangedListener {
            if(it?.toString().isNullOrBlank()) {
                binding.roomTitleEditText.error = "1文字以上入力する必要があります"
            }
        }
    }
}