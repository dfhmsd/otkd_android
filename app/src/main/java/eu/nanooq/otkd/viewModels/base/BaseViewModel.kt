package eu.nanooq.otkd.viewModels.base

import android.os.Bundle
import eu.inloop.viewmodel.AbstractViewModel
import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.App
import eu.nanooq.otkd.apiService.ApiProvider
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.helpers.PreferencesHelper
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * Created by rd on 26/07/2017.
 */
abstract class BaseViewModel<T: IView> : AbstractViewModel<T>() {


    @Inject
    lateinit var mFirebaseHelper : FirebaseHelper

    @Inject
    lateinit var mPreferencesHelper: PreferencesHelper

    @Inject
    lateinit var mApiProvider: ApiProvider


    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(arguments, savedInstanceState)

        App.component.inject(this as BaseViewModel<IView>)

    }
}
