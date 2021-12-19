package nl.marc_apps.ovgo.data

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val trainStationCacheDatePreference = longPreferencesKey("TRAIN_STATION_CACHE_DATE")

    val lastTrainStation = stringPreferencesKey("LAST_TRAIN_STATION")
}
