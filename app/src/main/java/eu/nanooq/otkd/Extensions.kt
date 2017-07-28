package eu.nanooq.otkd

import android.content.Context
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.nanooq.otkd.di.IDependency
import timber.log.Timber

/**
 *
 * Created by rd on 26/07/2017.
 */
fun IDependency.init() {
    Timber.d("initExt")
}

fun Context.s(@StringRes id: String): String{
        return getString(resources.getIdentifier(id, "string", packageName))
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}