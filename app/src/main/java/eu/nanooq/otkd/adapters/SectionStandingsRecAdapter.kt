package eu.nanooq.otkd.adapters


import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.formatTimeInMinutes
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.CompleteStandingItemTypes
import eu.nanooq.otkd.models.UI.ItemType
import eu.nanooq.otkd.models.UI.SectionStandingItem
import kotlinx.android.synthetic.main.item_complete_standing_header.view.*
import kotlinx.android.synthetic.main.item_complete_standing_result.view.*

/**
 *
 * Created by rd on 04/08/2017.
 */
class SectionStandingsRecAdapter(var mList: ArrayList<ItemType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addItems(newItems: ArrayList<ItemType>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewType = mList[position].getViewType()
        when (viewType) {
            CompleteStandingItemTypes.HEADER -> {
                holder as TeamSectionStandingHeaderVH
                holder.bind()
            }
            CompleteStandingItemTypes.ITEM -> {
                holder as TeamSectionStandingVH
                holder.bind(mList[position] as SectionStandingItem)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            CompleteStandingItemTypes.HEADER -> {
                TeamSectionStandingHeaderVH(parent)
            }

            CompleteStandingItemTypes.ITEM -> {
                TeamSectionStandingVH(parent)
            }

            else -> TeamStandingHeaderVH(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].getViewType()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}

class TeamSectionStandingVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_complete_standing_result)) {
    fun bind(item: SectionStandingItem) = itemView.apply {
        with(item) {
            vStandingValue.text = "$mStanding."
            vTeamName.text = mTeamName //
            vTeamTime.text = mTeamTime.formatTimeInMinutes()
            vCategoryStanding.text = mCompleteTime.formatTimeInMinutes()
            if (mIsYourTeam) {
                item_complete_standing_result.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }
}

class TeamSectionStandingHeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_complete_standing_header)) {
    fun bind() = itemView.apply {
        vCategoryStandingLabel.text = context.getString(R.string.complete_time)
    }
}