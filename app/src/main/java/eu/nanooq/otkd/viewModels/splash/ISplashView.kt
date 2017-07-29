package eu.nanooq.otkd.viewModels.splash

import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.models.API.User

/**
 *
 * Created by rd on 27/07/2017.
 */
interface ISplashView : IView {
    fun onLoginCaptainClick()

    fun onLoginRunnerClick()
    fun  autoSignInUser(user: User)
    fun  startLoginActivity(captain: Boolean)
}
