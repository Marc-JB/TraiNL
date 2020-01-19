package nl.marc_apps.ovgo.domain.models

data class TrainComposition(
    val id: Short,
    val station: TrainStation?,
    val type: String,
    val operator: String,
    val platform: String,
    val parts: Array<TrainPart>,
    val shortened: Boolean,
    val actualNumberOfCoaches: Byte,
    val plannedNumberOfCoaches: Byte,
    val length: Short,
    //val crowdsForecast: Array<Object>,
    val facilities: TrainFacilities,
    val seats: Short,
    val seatsFirstClass: Short
){
    val images
        get() = Array(4){ parts.getOrNull(it)?.image }
}