package eu.nanooq.otkd.viewModels.userDetail

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.UI.UserDetailItem

/**
 *
 * Created by rd on 07/08/2017.
 */
interface IUserDetailView : IView {
    fun updateData(userDetail: UserDetailItem)
    fun setToolbarTitle(title: String)

}
