package nl.marc_apps.ovgo.utils

val Boolean.asBit
    get() = if(this) 0b1 else 0b0
