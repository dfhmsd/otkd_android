package eu.nanooq.otkd.di

import android.content.Context
import eu.nanooq.otkd.App
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * Created by rd on 26/07/2017.
 */
interface IDependency {

    fun init()
    fun destroy()
}