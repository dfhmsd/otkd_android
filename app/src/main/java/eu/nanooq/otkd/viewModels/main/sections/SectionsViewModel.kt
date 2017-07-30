package eu.nanooq.otkd.viewModels.main.sections

import android.os.Bundle
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import timber.log.Timber

/**
 *
 * Created by rd on 28/07/2017.
 */
class SectionsViewModel : BaseViewModel<ISectionsView>() {

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)
    }
}
