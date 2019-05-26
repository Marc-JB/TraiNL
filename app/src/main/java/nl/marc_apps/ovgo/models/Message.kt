package nl.marc_apps.ovgo.models

data class Message(val message: String, val type: String){
    override fun toString() = "$type: $message"
}