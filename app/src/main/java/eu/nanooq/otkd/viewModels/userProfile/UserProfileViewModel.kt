package eu.nanooq.otkd.viewModels.userProfile

import eu.nanooq.otkd.viewModels.base.BaseViewModel

/**
 *
 * Created by rd on 31/07/2017.
 */
class UserProfileViewModel : BaseViewModel<IUserProfileView>() {
    fun logoff() {
        mPreferencesHelper.deleteUserData()
        view?.logout()
    }

}
