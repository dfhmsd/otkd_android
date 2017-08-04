package eu.nanooq.otkd.viewModels.sectionDetail

import android.os.Bundle
import com.google.gson.Gson
import eu.nanooq.otkd.models.UI.SectionItem
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 31/07/2017.
 */
class RunnerSectionDetailVievModel : BaseViewModel<IRunnerSectionDetailView>() {

    lateinit var sectionItem: SectionItem

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)
        val sectionJson = arguments?.getString("sectionItem")

        sectionItem = Gson().fromJson(sectionJson, SectionItem::class.java) ?: SectionItem()

    }

    fun getData() {
        view?.setupMap(sectionItem)
        view?.setupRunner(sectionItem)
    }

    fun openTrackDetail() {
        view?.startTrackDetail(sectionItem.Id)
    }

}
