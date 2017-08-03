package eu.nanooq.otkd.viewModels.login

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.API.User

/**
 *
 * Created by rd on 23/07/2017.
 */
interface ILoginView : IView {
    fun onUserLogedIn(user: User)
    fun finishLoginActivity()
    fun showCaptainLoginOptions()
    fun showRunnerLoginOptions()
    fun showError(errorMsg: String)
    fun showProgressBar()
    fun dismissProgressBar()
}
