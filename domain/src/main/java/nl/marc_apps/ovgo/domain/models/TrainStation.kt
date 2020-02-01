package nl.marc_apps.ovgo.domain.models

data class TrainStation(
    val id: Int,
    val code: String,
    val name: String,
    val country: Country,
    val facilities: TrainStationFacilities,
    val coordinates: Coordinates,
    val alternativeNames: Array<String>,
    val platforms: Array<String>
){
    override fun toString() = name + if(country.code != "NL") " ${country.flag}" else ""
}