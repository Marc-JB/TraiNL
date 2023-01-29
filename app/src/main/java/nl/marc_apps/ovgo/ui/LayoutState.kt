package nl.marc_apps.ovgo.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

class LayoutState {
    var isLandscape: Boolean by mutableStateOf(false)
        private set

    var deviceClass by mutableStateOf(DeviceClass.SMALL)
        private set

    var windowPadding: DpSize by mutableStateOf(PADDING_SMALL_DEVICE_PORTRAIT)
        private set

    fun updateState(windowConstraints: BoxWithConstraintsScope) {
        isLandscape = windowConstraints.maxWidth > windowConstraints.maxHeight

        val minSize = minOf(windowConstraints.maxWidth, windowConstraints.maxHeight)
        deviceClass = if(minSize < 600.dp) {
            DeviceClass.SMALL
        } else if (minSize < 1240.dp) {
            DeviceClass.MEDIUM
        } else {
            DeviceClass.LARGE
        }

        windowPadding = when {
            deviceClass == DeviceClass.LARGE && isLandscape -> PADDING_LARGE_DEVICE_LANDSCAPE
            deviceClass == DeviceClass.LARGE -> PADDING_LARGE_DEVICE_PORTRAIT
            deviceClass == DeviceClass.MEDIUM && isLandscape -> PADDING_MEDIUM_DEVICE_LANDSCAPE
            deviceClass == DeviceClass.MEDIUM -> PADDING_MEDIUM_DEVICE_PORTRAIT
            deviceClass == DeviceClass.SMALL && isLandscape -> PADDING_SMALL_DEVICE_LANDSCAPE
            else -> PADDING_SMALL_DEVICE_PORTRAIT
        }
    }

    companion object {
        enum class DeviceClass { SMALL, MEDIUM, LARGE }

        private val PADDING_SMALL_DEVICE_PORTRAIT = DpSize(16.dp, 8.dp)
        private val PADDING_SMALL_DEVICE_LANDSCAPE = DpSize(32.dp, 8.dp)

        private val PADDING_MEDIUM_DEVICE_PORTRAIT = DpSize(32.dp, 16.dp)
        private val PADDING_MEDIUM_DEVICE_LANDSCAPE = DpSize(64.dp, 16.dp)

        private val PADDING_LARGE_DEVICE_PORTRAIT = DpSize(128.dp, 32.dp)
        private val PADDING_LARGE_DEVICE_LANDSCAPE = DpSize(200.dp, 32.dp)
    }
}

@Composable
fun rememberLayoutState(windowConstraints: BoxWithConstraintsScope): LayoutState {
    val layoutState = remember { LayoutState() }

    LaunchedEffect(windowConstraints) {
        layoutState.updateState(windowConstraints)
    }

    return layoutState
}
