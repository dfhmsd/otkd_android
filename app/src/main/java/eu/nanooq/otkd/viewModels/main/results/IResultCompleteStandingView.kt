package eu.nanooq.otkd.viewModels.main.results

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.ItemType

/**
 *
 * Created by rd on 03/08/2017.
 */
interface IResultCompleteStandingView : IView {

    fun updateAdapter(newItems: ArrayList<ItemType>)
    fun updateTeamsStats(finishedCount: Int, runningCount: Int)
}
