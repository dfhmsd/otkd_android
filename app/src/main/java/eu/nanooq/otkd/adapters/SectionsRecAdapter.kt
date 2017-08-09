package eu.nanooq.otkd.adapters

import android.content.Context
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


/**
 *
 * Created by rd on 29/07/2017.
 */
class SectionsRecAdapter(private var mList: ArrayList<SectionItem>, context: Context, val listener: (SectionItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val unassignedDrawable = RoundedBitmapDrawableFactory
            .create(context.resources, BitmapFactory.decodeResource(context.resources, R.drawable.ic_placeholder_unassigned))
            .apply {
                isCircular = true
            }
    val defaultUserDrawable = RoundedBitmapDrawableFactory
            .create(context.resources, BitmapFactory
                    .decodeResource(context.resources, R.drawable.ic_default_user)).apply {
        isCircular = true
    }


    fun addItems(newItems: ArrayList<SectionItem>) {
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SectionItemVH
        holder.bind(mList[position], listener, unassignedDrawable, defaultUserDrawable)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = SectionItemVH(parent)

    override fun getItemCount(): Int {
        return mList.size
    }


    class SectionItemVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_section)) {
        fun bind(item: SectionItem, listener: (SectionItem) -> Unit, placeholder: Drawable, defaultUserDrawable: Drawable) = itemView.apply {
            with(item) {
                vSectionId.text = "Úsek $Id"
                vSectionName.text = name.toUpperCase()
                vSectionLength.text = "Dĺžka: $length km"
//            vSectionCrossFall.text = "Previšenie (cca) $high - $down"
                vSectionDifficulty.text = "Obtiažnosť: $difficulty"
                vSectionRunnerName.text = runnerName
                if (runnerName.isBlank()) {
                    Glide
                            .with(context)
                            .load(R.drawable.ic_placeholder_unassigned)
                            .asBitmap()
                            .centerCrop()
                            .into(object : BitmapImageViewTarget(vSectionRunnerImage) {
                                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                                    super.onLoadFailed(e, errorDrawable)
                                    Timber.e("onLoadFailed")

                                    vSectionRunnerImage.setImageDrawable(placeholder)
                                }

                                override fun setResource(resource: Bitmap) {
                                    Timber.d("setResource")
                                    vSectionRunnerImage.setImageDrawable(placeholder)
                                }
                            })
                } else {
                    Glide
                            .with(context)
                            .load(runnerImgUrl)
                            .asBitmap()
                            .centerCrop()
                            .into(object : BitmapImageViewTarget(vSectionRunnerImage) {
                                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                                    super.onLoadFailed(e, errorDrawable)
                                    Timber.e("onLoadFailed")

                                    vSectionRunnerImage.setImageDrawable(defaultUserDrawable)
                                }

                                override fun setResource(resource: Bitmap) {
                                    Timber.d("setResource")
                                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                                    circularBitmapDrawable.isCircular = true
                                    vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                                }
                            })
                }

                itemView.setOnClickListener { listener(this) }

            }

        }
    }
}