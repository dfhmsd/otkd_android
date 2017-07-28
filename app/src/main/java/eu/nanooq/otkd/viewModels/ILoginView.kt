package eu.nanooq.otkd.viewModels

import eu.inloop.viewmodel.IView

/**
 *
 * Created by rd on 23/07/2017.
 */
interface ILoginView : IView {
    fun onUserLogedIn()
    fun finishLoginActivity()
    fun showCaptainLoginOptions()
    fun showRunnerLoginOptions()
    fun showError(errorMsg: String)
    fun showProgressBar()
    fun hideProgressBar()
}
