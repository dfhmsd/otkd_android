package eu.nanooq.otkd.activities

import android.os.Bundle
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.fragments.SectionsFragment
import eu.nanooq.otkd.viewModels.main.IMainView
import eu.nanooq.otkd.viewModels.main.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class MainActivity : ViewModelActivity<IMainView, MainViewModel>() , IMainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val sectionsFrag = SectionsFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.vMainContent, sectionsFrag)
                .commitAllowingStateLoss()


    }

    override fun onNewResult() {
    }
}
