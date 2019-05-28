package nl.marc_apps.ovgo.domainmodels

data class TrainComposition(
    val shortened: Boolean,
    val length: Number,
    val plannedLength: Number,
    val parts: Array<Train>
){
    val images
        get() = Array(4){ parts.getOrNull(it)?.image }
}