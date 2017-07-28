package eu.nanooq.otkd.activities.base

import android.content.Context
import android.os.Bundle
import eu.inloop.viewmodel.AbstractViewModel
import eu.inloop.viewmodel.IView
import eu.inloop.viewmodel.base.ViewModelBaseActivity
import eu.nanooq.otkd.App
import eu.nanooq.otkd.viewModels.base.ActivityViewModel
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 *
 * Created by rd on 26/07/2017.
 */

abstract class ViewModelActivity<T : IView, R : AbstractViewModel<T>> : ViewModelBaseActivity<T, R>(), IView {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        setModelView(this as T)

        App.component.inject(this as ViewModelActivity<IView, ActivityViewModel<IView>>)
    }

    override fun attachBaseContext(newBase: Context?) {
        Timber.d("attachBaseContext()")
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
