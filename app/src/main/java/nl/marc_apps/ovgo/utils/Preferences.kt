package nl.marc_apps.ovgo.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.firstOrNull

suspend fun <T> DataStore<Preferences>.getOrNull(key: Preferences.Key<T>): T? {
    return data.firstOrNull()?.get(key)
}
