package eu.nanooq.otkd.viewModels.main.sections

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.SectionItem

/**
 *
 * Created by rd on 29/07/2017.
 */
interface IUserSectionsView : IView {
    fun updateAdapter(list: ArrayList<SectionItem>)
}
