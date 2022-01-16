package nl.marc_apps.ovgo.utils

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nl.marc_apps.ovgo.BuildConfig

fun println(tag: String, message: String) {
    Firebase.crashlytics.log("$tag: $message")
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    } else {
        Log.v(tag, message)
    }
}
