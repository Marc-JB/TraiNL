package nl.marc_apps.ovgo.api.models

import kotlinx.serialization.Serializable

@Serializable
data class NsResponse<T>(val payload: T)
