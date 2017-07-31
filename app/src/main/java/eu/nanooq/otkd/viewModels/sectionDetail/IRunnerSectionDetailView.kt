package eu.nanooq.otkd.viewModels.sectionDetail

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.SectionItem

/**
 *
 * Created by rd on 31/07/2017.
 */
interface IRunnerSectionDetailView : IView {
    fun setupMap(item: SectionItem)
    fun setupRunner(item: SectionItem)

}
