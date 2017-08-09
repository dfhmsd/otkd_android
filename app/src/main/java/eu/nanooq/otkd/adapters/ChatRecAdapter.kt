package eu.nanooq.otkd.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.MessageItem
import kotlinx.android.synthetic.main.item_message.view.*
import timber.log.Timber
import java.util.*


/**
 *
 * Created by rd on 29/07/2017.
 */
class ChatRecAdapter(private var mList: ArrayList<MessageItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun addItems(newItems: ArrayList<MessageItem>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as ChatResultVH
        holder.bind(mList[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ChatResultVH(parent)

    override fun getItemCount(): Int {
        return mList.size
    }
}

class ChatResultVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_message)) {
    fun bind(item: MessageItem) = itemView.apply {
        with(item) {
            val msg = timestamp.toString() + ":" + sender + ":" + text
            Timber.d("ChatResultVH: $msg")
            vMessage.text = text
            vSender.text = sender
            val date = Date(item.timestamp)
            val s = android.text.format.DateFormat.format("dd.MM.yy HH:mm", date)
            vDate.text = s
            if (isCaptain) {
                vMessage.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))

            }
            if (isUser) {
                vIncomingCorner.setColorFilter(ContextCompat.getColor(context, R.color.yourMessage))
                vYourCorner.setColorFilter(ContextCompat.getColor(context, R.color.yourMessage))
                vCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yourMessage))
                vIncomingCorner.visibility = View.GONE
                vYourCorner.visibility = View.VISIBLE
            } else {
                vIncomingCorner.visibility = View.VISIBLE
                vYourCorner.visibility = View.GONE
            }
        }
    }
}
