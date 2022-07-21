package nl.marc_apps.ovgo.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R

@Composable
fun Card(
    applyHorizontalPadding: Boolean = true,
    content: @Composable ColumnScope.(Dp) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_radius)),
        color = MaterialTheme.colors.surface
    ) {
        val padding = dimensionResource(R.dimen.departure_details_card_padding)
        Column(
            modifier = Modifier
                .padding(if(applyHorizontalPadding) padding else 0.dp, padding)
                .fillMaxWidth(),
            content = {
                content(padding)
            }
        )
    }
}
