package eu.nanooq.otkd.viewModels.splash

import android.os.Bundle
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 27/07/2017.
 */
class SplashViewModel : BaseViewModel<ISplashView>() {

    companion object {
        const val CAPTAIN = "captain"
    }

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)

    }

    fun onLoginCaptainClick() {
        Timber.d("onLoginCaptainClick()")
        view?.startLoginActivity(true)
    }

    fun onLoginRunnerClick() {
        Timber.d("onLoginRunnerClick()")

        view?.startLoginActivity(false)
    }

    fun checkIfUserIsAlreadyLogged() {
        val user = mPreferencesHelper.getUser()
        Timber.d("onLoginRunnerClick() $user")
        user?.run { view?.autoSignInUser(user) }
    }
}
