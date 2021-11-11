package jp.panta.firechatapp.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.panta.firechatapp.databinding.FragmentMessagesBinding
import jp.panta.firechatapp.databinding.ItemMessageBinding
import jp.panta.firechatapp.models.Message
import jp.panta.firechatapp.models.MessageView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessagesFragment : Fragment(){

    private var _binding: FragmentMessagesBinding? = null
    private val binding: FragmentMessagesBinding
        get() = _binding!!

    private val args: MessagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messagesViewModel: MessagesViewModel = ViewModelProvider(
            this,
            MessagesViewModel.Factory(args.roomId)
        )[MessagesViewModel::class.java]

        val adapter = object : ListAdapter<MessageView, MessageViewHolder>(
            object : DiffUtil.ItemCallback<MessageView> (){
                override fun areContentsTheSame(oldItem: MessageView, newItem: MessageView): Boolean {
                    return oldItem == newItem
                }

                override fun areItemsTheSame(oldItem: MessageView, newItem: MessageView): Boolean {
                    return oldItem.id == newItem.id
                }
            }
        ) {
            override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
                return holder.bind(getItem(position))
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                return MessageViewHolder(
                    ItemMessageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),

                )
            }
        }
        binding.messagesView.adapter = adapter
        binding.messagesView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            messagesViewModel.messages.collect {
                adapter.submitList(it)
            }
        }

    }
}

class MessageViewHolder(
    private val binding: ItemMessageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: MessageView) {
        binding.messageText.text = message.text
        binding.usernameView.text = message.user?.username
    }


}