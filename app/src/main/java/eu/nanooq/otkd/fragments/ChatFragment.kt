package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.ChatRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.MessageItem
import eu.nanooq.otkd.viewModels.chat.ChatViewModel
import eu.nanooq.otkd.viewModels.main.sections.IChatView
import kotlinx.android.synthetic.main.fragment_chat.*
import timber.log.Timber


/**
 *
 * Created by rd on 31/07/2017.
 */
class ChatFragment : ViewModelFragment<IChatView, ChatViewModel>(), IChatView {
    lateinit var mAdapter: ChatRecAdapter

    companion object {
        fun newInstance(): ChatFragment {
            Timber.d("newInstance()")

            return ChatFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_chat)

        mAdapter = ChatRecAdapter(ArrayList())
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()

        vAllMessagesRecView.adapter = mAdapter
        vAllMessagesRecView.layoutManager = LinearLayoutManager(context)

        vSendButton.setOnClickListener {
            sendMessage()
        }
    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
//        val mToolbarCallback = activity as IActivityToolbar
//        mToolbarCallback.onToolbarTitleChange(getString(R.string.chat_toolbar_title))

    }

    private fun sendMessage() {
        if (vMyMessage.text.toString().isNotBlank()) {
            viewModel.sendMessage(vMyMessage.text.toString())
            vMyMessage.setText("")
        }
    }

    override fun updateAdapter(newItems: ArrayList<MessageItem>) {
        Timber.d("updateAdapter() $newItems")
        mAdapter.addItems(newItems)
        vAllMessagesRecView?.scrollToPosition(newItems.size - 1)
    }
}