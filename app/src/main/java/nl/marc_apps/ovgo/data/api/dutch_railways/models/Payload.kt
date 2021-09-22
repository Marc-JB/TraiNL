package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable

@Serializable
data class Payload<T>(
    val payload: T
)
