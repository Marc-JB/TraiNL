package nl.marc_apps.ovgo.ui

import android.content.Context
import nl.marc_apps.ovgo.domain.services.UserPreferences

class UserPreferencesProvider(context: Context) : UserPreferences {
    override val language: String = context.getString(R.string.languageCode)

    override var station: String = "Dordrecht"
}