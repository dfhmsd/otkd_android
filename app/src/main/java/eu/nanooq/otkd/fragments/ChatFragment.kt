package eu.nanooq.otkd.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.gson.Gson
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.SectionDetailActivity
import eu.nanooq.otkd.adapters.ChatRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.MessageItem
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.chat.ChatViewModel
import eu.nanooq.otkd.viewModels.main.sections.ChatView
import kotlinx.android.synthetic.main.fragment_chat.*
import timber.log.Timber


/**
 *
 * Created by rd on 31/07/2017.
 */
class ChatFragment : ViewModelFragment<ChatView, ChatViewModel>(), ChatView {
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

        mAdapter = ChatRecAdapter(ArrayList(), context) {
            onDetailItemClick(it)
        }
        return view
    }

    private fun onDetailItemClick(messageItem: MessageItem) {
        val intent = Intent(context, SectionDetailActivity::class.java)
        intent.putExtra("item", Gson().toJson(messageItem))
        startActivity(intent)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        val mToolbarCallback = activity as IActivityToolbar

        mAdapter.hasStableIds()

        vAllMessagesRecView.adapter = mAdapter
        vAllMessagesRecView.isNestedScrollingEnabled = false
        vAllMessagesRecView.layoutManager = LinearLayoutManager(context)
        vAllMessagesRecView.hasFixedSize()

        vSendButton.setOnClickListener {
            sendMessage()
        }
        mToolbarCallback.onToolbarTitleChange(getString(R.string.chat_toolbar_title))
    }

    private fun sendMessage() {
        viewModel.sendMessage(vMyMessage.text.toString())
        vMyMessage.setText("")
    }

    override fun updateAdapter(newItems: ArrayList<MessageItem>) {
        Timber.d("updateAdapter() $newItems")
        mAdapter.addItems(newItems)
    }
}