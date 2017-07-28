package eu.nanooq.otkd.viewModels.base

import android.os.Bundle
import eu.inloop.viewmodel.AbstractViewModel
import eu.inloop.viewmodel.IView
import eu.nanooq.otkd.App
import eu.nanooq.otkd.apiService.ApiProvider
import eu.nanooq.otkd.helpers.FirebaseHelper
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * Created by rd on 26/07/2017.
 */
abstract class ActivityViewModel<T: IView> : AbstractViewModel<T>() {


    @Inject
    lateinit var mFirebaseHelper : FirebaseHelper

    @Inject
    lateinit var mApiProvider: ApiProvider

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(arguments, savedInstanceState)

        App.component.inject(this as ActivityViewModel<IView>)


    }
}
