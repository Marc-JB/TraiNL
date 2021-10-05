package nl.marc_apps.ovgo.utils

import android.app.ActivityManager
import android.content.Context
import androidx.core.app.ActivityManagerCompat
import androidx.core.content.getSystemService

val Context.isLowRamDevice: Boolean?
    get() {
        val activityManager = getSystemService<ActivityManager>()
        return activityManager?.let {
            ActivityManagerCompat.isLowRamDevice(it)
        }
    }
