package eu.nanooq.otkd.fragments.base

import android.os.Bundle
import android.view.View
import eu.inloop.viewmodel.IView
import eu.inloop.viewmodel.base.ViewModelBaseFragment
import eu.nanooq.otkd.App
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
abstract class ViewModelFragment<T: IView, R: BaseViewModel<T>> : ViewModelBaseFragment<T, R >() , IView {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        setModelView(this as T)

        App.component.inject(this as ViewModelFragment<IView, BaseViewModel<IView>>)
    }


}
