package nl.marc_apps.ovgo.ui.maintenance

import android.content.Context
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTrainInfoApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTravelInfoApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.test_utils.*
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.robolectric.annotation.Config
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class)
class MaintenanceFragmentTests {
    private fun createTestModule(response: Response<List<DutchRailwaysDisruption>>): Module {
        return module {
            single {
                mockk<DutchRailwaysTravelInfoApi> {
                    every {
                        getDisruptions(any(), any(), DutchRailwaysDisruption.DisruptionType.MAINTENANCE.name, any())
                    } returns mockCallWithResponse(response)
                }
            }

            single {
                mockk<DutchRailwaysTrainInfoApi>()
            }

            single {
                DutchRailwaysApi(get(), get(), "")
            }

            viewModel {
                MaintenanceViewModel(get())
            }
        }
    }

    private fun launchMaintenanceFragment(context: Context): FragmentScenario<MaintenanceFragment> {
        val testNavHostController = context.createCustomTestNavController(R.navigation.main_navigation) {
            setCurrentDestination(R.id.maintenance)
        }

        return launchFragmentInContainer(themeResId = R.style.Theme_OVgo) {
            MaintenanceFragment().withNavController(testNavHostController)
        }
    }

    @Test
    fun isPlaceholderShownWhenMaintenanceListIsEmpty() {
        withKoinModule(createTestModule(Response.success(200, emptyList()))) {
            launchMaintenanceFragment(it).onFragment {
                val placeholder = it.view?.findViewById<View>(R.id.partial_image_with_label_placeholder)
                assertNotNull(placeholder)
                assertEquals(View.VISIBLE, placeholder.visibility)
            }
        }
    }

    @Test
    fun isPlaceholderHiddenWhenMaintenanceListIsNotEmpty() {
        val testData = DutchRailwaysDisruption.DisruptionOrMaintenance(
            "abcd",
            DutchRailwaysDisruption.DisruptionType.MAINTENANCE,
            "MaIntEnANce",
            isActive = true,
            local = true,
            start = Clock.System.now(),
            publicationSections = emptyList(),
            timespans = emptyList()
        )

        withKoinModule(createTestModule(Response.success(200, listOf(testData)))) {
            launchMaintenanceFragment(it).onFragment {
                val placeholder = it.view?.findViewById<View>(R.id.partial_image_with_label_placeholder)
                assertNotNull(placeholder)
                assertEquals(View.GONE, placeholder.visibility)
            }
        }
    }
}
