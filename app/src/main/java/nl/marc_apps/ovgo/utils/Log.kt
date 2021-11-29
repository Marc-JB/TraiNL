package nl.marc_apps.ovgo.utils

import android.util.Log
import nl.marc_apps.ovgo.BuildConfig

fun println(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    } else {
        Log.v(tag, message)
    }
}
