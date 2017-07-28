package eu.nanooq.otkd.activities

import android.os.Bundle
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.viewModels.IMainView
import eu.nanooq.otkd.viewModels.MainViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class MainActivity : ViewModelActivity<IMainView, MainViewModel>() ,IMainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
    }

    override fun onNewResult() {
    }
}
