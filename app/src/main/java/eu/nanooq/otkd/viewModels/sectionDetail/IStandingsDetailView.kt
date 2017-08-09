package eu.nanooq.otkd.viewModels.sectionDetail

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.ItemType

/**
 *
 *Created by rd on 04/08/2017.
 */
interface IStandingsDetailView : IView {
    fun updateAdapter(newItems: ArrayList<ItemType>)
    fun updateTeamsStats(finishedCount: Int, runningCount: Int)
}