package eu.nanooq.otkd.viewModels.main.results

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.SectionResult

/**
 *
 * Created by rd on 03/08/2017.
 */
interface IResultPerSectionView : IView {

    fun updateAdapter(newItems: ArrayList<SectionResult>)
    fun updateLenghtOfAllSections(lenghtOfAllSections: Float)
    fun updateSectionsCount(size: Int)
    fun updateTeamsStartedCount(teamsStartedCount: Int)
    fun updateTeamsFinishedCount(teamsFinishedCount: Int)

}
