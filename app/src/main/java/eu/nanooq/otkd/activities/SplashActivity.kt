package eu.nanooq.otkd.activities

import android.os.Bundle
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.viewModels.ISplashView
import eu.nanooq.otkd.viewModels.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

/**
 *
 * Created by rd on 27/07/2017.
 */
class SplashActivity : ViewModelActivity<ISplashView , SplashViewModel>() , ISplashView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        mLogCaptain.setOnClickListener { onLoginCaptainClick() }
        mLogRunner.setOnClickListener { onLoginRunnerClick() }
    }

    override fun onLoginCaptainClick() {
        Timber.d("onLoginCaptainClick()")

        viewModel.onLoginCaptainClick(this)
    }

    override fun onLoginRunnerClick() {
        Timber.d("onLoginRunnerClick()")

        viewModel.onLoginRunnerClick(this)
    }
}
