package eu.nanooq.otkd

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import timber.log.Timber
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by rd on 26/07/2017.
 */

fun Context.s(@StringRes id: String): String {
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

fun String?.resultTime(): String {
    if (this == null) return ""

    var correctTime: String
    val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val output = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    try {
        correctTime = output.format(input.parse(this))
    } catch (exc: Exception) {
        Timber.e("Unable to parse time: $this to ${output.toPattern()}")
        correctTime = ""
    }
    return correctTime
}

fun Float?.formatTimeInMinutes(): String {
    if (this == null) return ""
    else {
        val hours = (this / 60).toInt()
        val restMinutes = (this % 60).toInt()
        val sec = restMinutes / 60
        val hoursString = String.format("%02d", hours)
        val minutesString = String.format("%02d", restMinutes)
        val secondsString = String.format("%02d", sec)
        return "$hoursString:$minutesString:$secondsString"
    }
}


