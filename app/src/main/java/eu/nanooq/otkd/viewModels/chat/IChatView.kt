package eu.nanooq.otkd.viewModels.main.sections

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.MessageItem

/**
 *
 * Created by rd on 28/07/2017.
 */
interface IChatView : IView {
    fun updateAdapter(newItems: ArrayList<MessageItem>)

}
