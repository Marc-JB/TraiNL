package nl.marc_apps.ovgo.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStation
import nl.marc_apps.ovgo.data.db.TrainStationDao
import nl.marc_apps.ovgo.data.db.TrainStationEntity
import nl.marc_apps.ovgo.domain.TrainStation
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TrainStationRepositoryTests {
    @Test
    fun getTrainStationByIdShouldReturnDatabaseResultBeforeApi() = runTest {
        // Arrange
        val trainStationDao = mockk<TrainStationDao>(relaxed = true)
        val dutchRailwaysApi = mockk<DutchRailwaysApi>(relaxed = true)
        val trainStationRepository = TrainStationRepository(
            trainStationDao,
            dutchRailwaysApi,
            mockk(relaxed = true)
        )

        val stationCode = "123abc"

        coEvery { trainStationDao.getByUicCode(stationCode) } returns TrainStationEntity(
            stationCode,
            stationCode,
            stationCode,
            "[]",
            "[]",
            false,
            false,
            TrainStation.Country.THE_NETHERLANDS
        )

        // Act
        val station = trainStationRepository.getTrainStationById(stationCode)

        // Assert
        coVerify(exactly = 1) { trainStationDao.getByUicCode(stationCode) }
        coVerify(exactly = 0) { dutchRailwaysApi.getTrainStations() }
        assertNotNull(station)
    }

    @Test
    fun getTrainStationByIdShouldReturnNullWhenNotFoundInDatabaseAndApi() = runTest {
        // Arrange
        val trainStationDao = mockk<TrainStationDao>(relaxed = true)
        val dutchRailwaysApi = mockk<DutchRailwaysApi>(relaxed = true)
        val trainStationRepository = TrainStationRepository(
            trainStationDao,
            dutchRailwaysApi,
            mockk(relaxed = true)
        )

        val stationCode = "123abc"

        coEvery { trainStationDao.getByUicCode(stationCode) } returns null

        // Act
        val station = trainStationRepository.getTrainStationById(stationCode)

        // Assert
        coVerify(exactly = 1) { trainStationDao.getByUicCode(stationCode) }
        coVerify(exactly = 1) { dutchRailwaysApi.getTrainStations() }
        assertNull(station)
    }

    @Test
    fun getTrainStationByIdShouldReturnApiResult() = runTest {
        // Arrange
        val trainStationDao = mockk<TrainStationDao>(relaxed = true)
        val dutchRailwaysApi = mockk<DutchRailwaysApi>(relaxed = true)
        val trainStationRepository = TrainStationRepository(
            trainStationDao,
            dutchRailwaysApi,
            mockk(relaxed = true)
        )

        val stationCode = "123abc"
        val mockedStation = mockk<DutchRailwaysStation>(relaxed = true)
        every { mockedStation.uicCode } returns stationCode
        every { mockedStation.country } returns DutchRailwaysStation.Country.THE_NETHERLANDS

        coEvery { trainStationDao.getByUicCode(stationCode) } returns null
        coEvery { dutchRailwaysApi.getTrainStations() } returns listOf(mockedStation)

        // Act
        val station = trainStationRepository.getTrainStationById(stationCode)

        // Assert
        coVerify(exactly = 1) { trainStationDao.getByUicCode(stationCode) }
        coVerify(exactly = 1) { dutchRailwaysApi.getTrainStations() }
        assertNotNull(station)
    }
}
