package nl.marc_apps.ovgo.domain.models

data class TrainComposition(
    val id: Short? = null,
    val station: TrainStation?,
    val type: String? = null,
    val operator: String? = null,
    val platform: String? = null,
    val parts: Array<TrainPart>,
    val shortened: Boolean,
    val actualNumberOfCoaches: Byte? = null,
    val plannedNumberOfCoaches: Byte? = null,
    val length: Short?,
    //val crowdsForecast: Array<Object>,
    val facilities: TrainFacilities,
    val seats: Short,
    val seatsFirstClass: Short
){
    val images get() = parts.map { it.image }
}
