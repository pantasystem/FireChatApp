package jp.panta.firechatapp.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.panta.firechatapp.R
import jp.panta.firechatapp.databinding.FragmentRoomsBinding
import jp.panta.firechatapp.databinding.ItemRoomBinding
import jp.panta.firechatapp.models.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

class RoomsFragment : Fragment() {

    private var _binding: FragmentRoomsBinding? = null
    private val binding: FragmentRoomsBinding
        get() = _binding!!

    private val viewModel: RoomsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRoomButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_roomEditorFragment)
        }

        val adapter = object: ListAdapter<Room, RoomItemViewHolder>(
            object : DiffUtil.ItemCallback<Room>() {
                override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
                    return oldItem == newItem
                }

                override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
                    return oldItem.id == newItem.id
                }
            }
        ) {
            override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
                holder.bind(getItem(position))
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return RoomItemViewHolder(
                    ItemRoomBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    onClick = { v, r ->
                        val action = RoomsFragmentDirections.actionNavigationHomeToMessagesFragment(roomId = r.id)
                        v.findNavController().navigate(action)
                    }
                )
            }
        }

        binding.roomsView.adapter = adapter
        binding.roomsView.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launchWhenCreated {
            viewModel.rooms.collect {
                adapter.submitList(it)
            }
        }
    }
}

class RoomItemViewHolder(private val itemRoomBinding: ItemRoomBinding, private val onClick: (view: View, room: Room)->Unit = { _, _ -> }) : RecyclerView.ViewHolder(itemRoomBinding.root) {

    fun bind(room: Room) {
        itemRoomBinding.roomName.text = room.name
        itemRoomBinding.root.setOnClickListener {
            onClick.invoke(it, room)
        }
    }
}
