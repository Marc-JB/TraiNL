package nl.marc_apps.ovgo.ui.departure_details

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.test_utils.KoinTestApplication
import nl.marc_apps.ovgo.ui.preview.fixtures.TrainStationPreviewParameterProvider
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class)
class DepartureStopsViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @BeforeTest
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    @Test
    fun departureStopShouldShowSingleTimeViewWhenArrivalAndDepartureTimeAreEqual() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 2.minutes,
            now + 2.minutes,
            now + 2.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(1)
    }

    @Test
    fun departureStopShouldShowSingleTimeViewWhenDelayedArrivalAndDepartureTimeAreEqual() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 4.minutes,
            now + 2.minutes,
            now + 4.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(1)
    }

    @Test
    fun departureStopShouldShowSeparateTimesWhenDepartureIsLaterThanArrival() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 2.minutes,
            now + 4.minutes,
            now + 4.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(2)
    }

    @Test
    fun departureStopShouldShowSeparateTimesWhenDelayedDepartureIsLaterThanArrival() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 2.minutes,
            now + 2.minutes,
            now + 4.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(2)
    }

    @Test
    fun departureStopShouldShowSeparateTimesWhenArrivalIsDelayed() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 4.minutes,
            now + 4.minutes,
            now + 4.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(2)
    }

    @Test
    fun departureStopShouldShowSeparateTimesWhenDelayedDepartureIsLaterThanDelayedArrival() {
        // Arrange
        val now = Clock.System.now()
        val stop = JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            now + 2.minutes,
            now + 4.minutes,
            now + 4.minutes,
            now + 6.minutes,
            "3a",
            "3a"
        )

        // Act
        composeTestRule.setContent {
            DepartureStop(stop) {}
        }

        // Assert
        composeTestRule
            .onAllNodesWithTag("TimeDisplayView", useUnmergedTree = true)
            .assertCountEquals(2)
    }
}
