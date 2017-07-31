package eu.nanooq.otkd.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import eu.nanooq.otkd.R
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionItem
import kotlinx.android.synthetic.main.item_section.view.*
import timber.log.Timber
import java.lang.Exception


/**
 *
 * Created by rd on 29/07/2017.
 */
class SectionsRecAdapter(private var mList: ArrayList<SectionItem>, val listener: (SectionItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addItems(newItems: ArrayList<SectionItem>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SectionItemVH
        holder.bind(mList[position], listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = SectionItemVH(parent)

    override fun getItemCount(): Int = mList.size

}

class SectionItemVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_section)) {
    fun bind(item: SectionItem, listener: (SectionItem) -> Unit) = itemView.apply {
        with(item) {
            vSectionId.text = "Úsek $Id"
            vSectionName.text = name
            vSectionLength.text = "Dĺžka $length km"
            vSectionCrossFall.text = "Previšenie (cca) $high - $down"
            vSectionDifficulty.text = "Obtiažnost $difficulty"
            vSectionRunnerName.text = runnerName

            Glide
                    .with(context)
                    .load(runnerImgUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(vSectionRunnerImage) {
                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            Timber.e("onLoadFailed")
                            val iconBmp = BitmapFactory.decodeResource(resources, R.drawable.ic_default_user)
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, iconBmp)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }

                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }
                    })


            itemView.setOnClickListener { listener(this) }

        }

    }
}
