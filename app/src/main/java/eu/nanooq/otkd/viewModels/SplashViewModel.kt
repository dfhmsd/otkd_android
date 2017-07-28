package eu.nanooq.otkd.viewModels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.nanooq.otkd.activities.LoginActivity
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

    fun onLoginCaptainClick(context: Context) {
        Timber.d("onLoginCaptainClick()")

        val intent = Intent(context , LoginActivity::class.java )
        intent.putExtra(CAPTAIN , true)
        context.startActivity(intent)
    }

    fun onLoginRunnerClick(context: Context) {
        Timber.d("onLoginRunnerClick()")

        val intent = Intent(context , LoginActivity::class.java )
        intent.putExtra(CAPTAIN , false)
        context.startActivity(intent)
    }
}
