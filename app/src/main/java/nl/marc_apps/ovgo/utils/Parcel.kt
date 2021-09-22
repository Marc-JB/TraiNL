package nl.marc_apps.ovgo.utils

import android.os.Parcel

fun Parcel.readStringCollection(): Collection<String> {
    val list = mutableListOf<String>()
    readStringList(list)
    return list
}
