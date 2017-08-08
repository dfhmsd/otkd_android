package eu.nanooq.otkd.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.MessageItem
import kotlinx.android.synthetic.main.item_message.view.*


/**
 *
 * Created by rd on 29/07/2017.
 */
class ChatRecAdapter(private var mList: ArrayList<MessageItem>, context: Context, val listener: (MessageItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun addItems(newItems: ArrayList<MessageItem>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as ChatResultVH
        holder.bind(mList[position], listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ChatResultVH(parent)

    override fun getItemCount(): Int {
        return mList.size
    }
}

class ChatResultVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_message)) {
    fun bind(item: MessageItem, listener: (MessageItem) -> Unit) = itemView.apply {
        with(item) {
            vMessage.text = text
        }
    }
}
