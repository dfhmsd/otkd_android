package eu.nanooq.otkd.activities

import android.content.Intent
import android.os.Bundle
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.viewModels.splash.ISplashView
import eu.nanooq.otkd.viewModels.splash.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

/**
 *
 * Created by rd on 27/07/2017.
 */
class SplashActivity : ViewModelActivity<ISplashView, SplashViewModel>() , ISplashView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        viewModel.checkIfUserIsAlreadyLogged()
        setContentView(R.layout.activity_splash)

        mLogCaptain.setOnClickListener { onLoginCaptainClick() }
        mLogRunner.setOnClickListener { onLoginRunnerClick() }
    }

    override fun onLoginCaptainClick() {
        Timber.d("onLoginCaptainClick()")

        viewModel.onLoginCaptainClick()
    }

    override fun onLoginRunnerClick() {
        Timber.d("onLoginRunnerClick()")

        viewModel.onLoginRunnerClick()
    }

    override fun autoSignInUser(user: User) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    override fun startLoginActivity(captain: Boolean) {
        val intent = Intent(this , LoginActivity::class.java )
        intent.putExtra(SplashViewModel.CAPTAIN, captain)
        startActivity(intent)
    }
}
