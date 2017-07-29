package eu.nanooq.otkd.viewModels.main.sections

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.SectionItem

/**
 *
 * Created by rd on 28/07/2017.
 */
interface IAllSectionsView : IView {
    fun updateAdapter(newItems: ArrayList<SectionItem>)
}
