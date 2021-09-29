package nl.marc_apps.ovgo.utils

import android.os.Parcel
import android.os.Parcelable

fun Parcel.readStringCollection(): Collection<String> {
    val list = mutableListOf<String>()
    readStringList(list)
    return list
}

fun <T : Parcelable> Parcel.readTypedList(creator: Parcelable.Creator<T>): List<T> {
    val list = mutableListOf<T>()
    readTypedList(list, creator)
    return list
}

inline fun <reified T : Parcelable> Parcel.readParcelable(): T? {
    return readParcelable(T::class.java.classLoader)
}
