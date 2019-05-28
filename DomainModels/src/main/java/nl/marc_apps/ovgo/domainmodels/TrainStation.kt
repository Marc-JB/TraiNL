package nl.marc_apps.ovgo.domainmodels

data class TrainStation(val name: String, val uicCode: String){
    override fun toString() = "$name ($uicCode)"
}