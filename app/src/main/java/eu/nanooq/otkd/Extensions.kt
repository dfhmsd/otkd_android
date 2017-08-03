package eu.nanooq.otkd

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.nio.charset.Charset

/**
 *
 * Created by rd on 26/07/2017.
 */

fun Context.s(@StringRes id: String): String{
        return getString(resources.getIdentifier(id, "string", packageName))
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun String?.toBase64(): String? {
    if (this == null) return null
    return Base64.encodeToString(this.toByteArray(Charset.forName("UTF-8")), Base64.NO_WRAP)
}


