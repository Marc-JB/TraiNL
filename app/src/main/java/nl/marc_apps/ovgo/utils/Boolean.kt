package nl.marc_apps.ovgo.utils

inline val Boolean.asBit
    get() = if(this) 0b1 else 0b0
