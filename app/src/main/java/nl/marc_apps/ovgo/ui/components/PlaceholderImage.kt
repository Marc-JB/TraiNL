package nl.marc_apps.ovgo.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.theme.AppTheme

@Composable
fun PlaceholderImage(
    text: String,
    @DrawableRes imageId: Int,
    contentDescription: String = text
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .sizeIn(maxHeight = 250.dp, maxWidth = 350.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
    }
}

@DayNightPreview
@Composable
fun PlaceholderImagePreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            PlaceholderImage(
                text = stringResource(R.string.no_departures),
                imageId = R.drawable.va_stranded_traveler
            )
        }
    }
}
