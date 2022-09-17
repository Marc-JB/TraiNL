package nl.marc_apps.ovgo.ui.preview

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light theme",
    group = "themes",
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark theme",
    group = "themes",
    uiMode = UI_MODE_NIGHT_YES
)
annotation class DayNightPreview
