package nl.marc_apps.ovgo.data.api.dutch_railways.utils

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.Calamity
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionOrMaintenance
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionType.*

object DutchRailwaysDisruptionSerializer : JsonContentPolymorphicSerializer<DutchRailwaysDisruption>(DutchRailwaysDisruption::class) {
    private const val API_MODEL_TYPE_KEY = "type"

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out DutchRailwaysDisruption> {
        return when (val type = element.jsonObject[API_MODEL_TYPE_KEY]?.jsonPrimitive?.content) {
            CALAMITY.name -> Calamity.serializer()
            MAINTENANCE.name, DISRUPTION.name -> DisruptionOrMaintenance.serializer()
            else -> throw IllegalStateException("Unknown disruption type $type")
        }
    }
}
