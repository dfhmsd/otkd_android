package eu.nanooq.otkd.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionResult
import kotlinx.android.synthetic.main.item_section_result.view.*

/**
 *
 * Created by rd on 03/08/2017.
 */


class SectionResultsRecAdapter(private var mList: ArrayList<SectionResult>, private var listener: (SectionResult) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addItems(newItems: ArrayList<SectionResult>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SectionResultVH
        holder.bind(mList[position], listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = SectionResultVH(parent)

    override fun getItemCount(): Int {
        return mList.size
    }

}

class SectionResultVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_section_result)) {
    fun bind(item: SectionResult, listener: (SectionResult) -> Unit) = itemView.apply {
        with(item) {
            vSectionName.text = mSectionName.toUpperCase()
            vSectionTeams.text = when (mSectionTeamCount) {
                0 -> "$mSectionTeamCount tímov"
                1 -> "$mSectionTeamCount tím"
                in 2..4 -> "$mSectionTeamCount tímy"
                else -> "$mSectionTeamCount tímov"
            }
            vSectionNumberImage.text = mSectionId.toString()
            if (mSectionCompletedByYourTeam) {
                vSectionNumberImage.background = ContextCompat.getDrawable(context, R.drawable.result_circle_team)
            } else {
                vSectionNumberImage.background = ContextCompat.getDrawable(context, R.drawable.result_circle)

            }

            setOnClickListener { listener(this) }
        }
    }
}