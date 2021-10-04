package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.data.api.dutch_railways.utils.DutchRailwaysDisruptionSerializer
import nl.marc_apps.ovgo.utils.DateSerializer
import nl.marc_apps.ovgo.utils.NullableDateSerializer
import java.util.*

@Serializable(with = DutchRailwaysDisruptionSerializer::class)
sealed class DutchRailwaysDisruption {
    abstract val id: String
    abstract val type: DisruptionType
    abstract val title: String
    abstract val isActive: Boolean
    abstract val topic: String?

    @Serializable
    data class DisruptionOrMaintenance(
        override val id: String,
        override val type: DisruptionType,
        override val title: String,
        override val isActive: Boolean,
        val local: Boolean,
        @Serializable(with = DateSerializer::class)
        val start: Date,
        val publicationSections: List<PublicationSection>,
        val timespans: List<TimeSpan>,
        @Serializable(with = NullableDateSerializer::class)
        val registrationTime: Date? = null,
        @Serializable(with = NullableDateSerializer::class)
        val releaseTime: Date? = null,
        override val topic: String? = null,
        @Serializable(with = NullableDateSerializer::class)
        val end: Date? = null,
        val period: String? = null,
        val phase: Phase? = null,
        val impact: Impact? = null,
        val expectedDuration: ExpectedDuration? = null,
        val summaryAdditionalTravelTime: SummaryAdditionalTravelTime? = null,
        val alternativeTransportTimespans: List<AlternativeTransportTimespan> = emptyList()
    ) : DutchRailwaysDisruption() {
        @Serializable
        data class Phase(
            val id: String,
            val label: String
        )

        @Serializable
        data class Impact(
            val value: Int
        )

        @Serializable
        data class ExpectedDuration(
            val description: String,
            @Serializable(with = NullableDateSerializer::class)
            val endTime: Date? = null
        )

        @Serializable
        data class SummaryAdditionalTravelTime(
            val label: String,
            val shortLabel: String? = null,
            val minimumDurationInMinutes: Long? = null,
            val maximumDurationInMinutes: Long? = null
        )

        @Serializable
        data class PublicationSection(
            val section: Section,
            val consequence: Consequence? = null
        ) {
            @Serializable
            data class Section(
                val direction: Direction,
                val stations: List<Station>
            ) {
                @Serializable
                data class Station(
                    val uicCode: String,
                    val name: String,
                    val countryCode: String,
                    val stationCode: String? = null,
                    val coordinate: Coordinates? = null
                ) {
                    @Serializable
                    data class Coordinates(
                        val lat: Double,
                        val lng: Double
                    )
                }

                enum class Direction {
                    ONE_WAY, BOTH
                }
            }

            @Serializable
            data class Consequence(
                val level: Level,
                val section: Section,
                val description: String? = null
            ) {
                enum class Level {
                    NO_OR_MUCH_LESS_TRAINS,
                    LESS_TRAINS,
                    NORMAL_OR_MORE_TRAINS
                }
            }
        }

        @Serializable
        data class TimeSpan(
            @Serializable(with = DateSerializer::class)
            val start: Date,
            val situation: Situation,
            val advices: List<String> = emptyList(),
            @Serializable(with = NullableDateSerializer::class)
            val end: Date? = null,
            val period: String? = null,
            val cause: Cause? = null,
            val additionalTravelTime: AdditionalTravelTime? = null,
            val alternativeTransport: AlternativeTransport? = null
        ) {
            @Serializable
            data class Situation(
                val label: String
            )

            @Serializable
            data class Cause(
                val label: String
            )

            @Serializable
            data class AdditionalTravelTime(
                val label: String,
                val shortLabel: String? = null,
                val minimumDurationInMinutes: Long? = null,
                val maximumDurationInMinutes: Long? = null
            )
        }

        @Serializable
        data class AlternativeTransport(
            val label: String,
            val shortLabel: String? = null
        )

        @Serializable
        data class AlternativeTransportTimespan(
            @Serializable(with = NullableDateSerializer::class)
            val start: Date? = null,
            @Serializable(with = NullableDateSerializer::class)
            val end: Date? = null,
            val alternativeTransport: AlternativeTransport
        )
    }

    @Serializable
    data class Calamity(
        override val id: String,
        override val type: DisruptionType,
        override val title: String,
        override val isActive: Boolean,
        val priority: Priority,
        override val topic: String? = null,
        val description: String? = null,
        @Serializable(with = NullableDateSerializer::class)
        val lastUpdated: Date? = null,
        @Serializable(with = NullableDateSerializer::class)
        val expectedNextUpdate: Date? = null
    ) : DutchRailwaysDisruption() {
        enum class Priority {
            PRIO_1, PRIO_2, PRIO_3
        }
    }

    enum class DisruptionType {
        CALAMITY, DISRUPTION, MAINTENANCE
    }
}
