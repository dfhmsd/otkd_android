package eu.nanooq.otkd.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import eu.nanooq.otkd.R
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionItem
import kotlinx.android.synthetic.main.item_section.view.*

/**
 *
 * Created by rd on 29/07/2017.
 */
class SectionsRecAdapter(private var mList: ArrayList<SectionItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addItems(newItems: ArrayList<SectionItem>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SectionItemVH
        holder.bind(mList[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = SectionItemVH(parent)

    override fun getItemCount(): Int = mList.size

}

class SectionItemVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_section)) {
    fun bind(item: SectionItem) = itemView.apply {
        with(item) {
            vSectionId.text = Id.toString()
            vSectionName.text = name
            vSectionLength.text = length.toString()
            vSectionCrossFall.text = crossFall
            vSectionDifficulty.text = difficulty.toString()
            vSectionRunnerName.text = runnerName

            //TODO load runnerImg
        }

    }
}
