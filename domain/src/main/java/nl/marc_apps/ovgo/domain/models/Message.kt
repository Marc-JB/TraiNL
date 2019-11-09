package nl.marc_apps.ovgo.domain.models

data class Message(val message: String, val type: String){
    override fun toString() = "$type: $message"
}