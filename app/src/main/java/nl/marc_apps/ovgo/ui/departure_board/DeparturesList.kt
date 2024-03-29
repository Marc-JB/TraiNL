package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.LayoutState
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import kotlin.math.min

@Composable
fun DeparturesList(
    departures: List<Departure>,
    layoutState: LayoutState = LayoutState(),
    imageLoader: ImageLoader? = null,
    onDepartureSelected: (Departure) -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background

    val listState = rememberLazyListState()

    val gradientSize = with(LocalDensity.current) {
        32.dp.toPx()
    }

    val startGradientSize by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                gradientSize
            } else {
                min(
                    gradientSize,
                    listState.firstVisibleItemScrollOffset * 1.5f
                )
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            // Workaround to enable alpha compositing
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                val colorsStart = listOf(Color.Transparent, backgroundColor)
                val colorsEnd = listOf(backgroundColor, Color.Transparent)
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colorsStart, endY = startGradientSize),
                    blendMode = BlendMode.DstIn
                )
                drawRect(
                    brush = Brush.verticalGradient(colorsEnd, startY = size.height - 32.dp.toPx()),
                    blendMode = BlendMode.DstIn
                )
            },
        state = listState
    ) {
        itemsIndexed(
            departures,
            key = { _, item -> item.journeyId },
            contentType = { _, item -> item.isCancelled }
        ) { index, departure ->
            DepartureView(departure, layoutState, imageLoader, onDepartureSelected)

            if (index != departures.lastIndex) {
                Divider(
                    modifier = Modifier.padding(layoutState.windowPadding.width, 0.dp)
                )
            } else {
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun DepartureView(
    departure: Departure,
    layoutState: LayoutState = LayoutState(),
    imageLoader: ImageLoader? = null,
    onDepartureSelected: (Departure) -> Unit
) {
    if (departure.isCancelled) {
        CancelledDepartureView(departure, layoutState)
    } else {
        ActiveDepartureView(departure, layoutState, imageLoader, onDepartureSelected)
    }
}

@DayNightPreview
@Composable
fun DeparturesListPreview() {
    val departures = DeparturePreviewParameterProvider().values.toList().mapIndexed { index, departure ->
        if (index == 0) {
            departure.copy(isCancelled = true)
        } else departure
    }

    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DeparturesList(departures) {}
        }
    }
}
