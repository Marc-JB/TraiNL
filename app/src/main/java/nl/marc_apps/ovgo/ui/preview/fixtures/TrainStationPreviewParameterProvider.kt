package nl.marc_apps.ovgo.ui.preview.fixtures

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nl.marc_apps.ovgo.domain.TrainStation

class TrainStationPreviewParameterProvider : PreviewParameterProvider<TrainStation> {
    override val values = sequenceOf(
        TrainStation("ddr", "Dordrecht", "Dordrecht"),
        TrainStation("rtd", "Rotterdam Centraal", "Rotterdam", setOf("Rotterdam CS", "Rotterdam"))
    )
}
