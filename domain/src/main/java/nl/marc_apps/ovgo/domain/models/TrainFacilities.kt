package nl.marc_apps.ovgo.domain.models

data class TrainFacilities(
    val toilet: Boolean,
    val silenceCompartment: Boolean,
    val powerSockets: Boolean,
    val wifi: Boolean,
    val wheelchairAccessible: Boolean,
    val bicycles: Boolean,
    val bar: Boolean,
    val firstClas: Boolean
)