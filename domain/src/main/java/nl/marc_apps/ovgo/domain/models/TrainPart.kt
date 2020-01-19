package nl.marc_apps.ovgo.domain.models

data class TrainPart(
    val id: String,
    val type: String,
    val facilities: TrainFacilities,
    val image: String,
    val seats: Short,
    val seatsFirstClass: Short,
    val destination: TrainStation
)