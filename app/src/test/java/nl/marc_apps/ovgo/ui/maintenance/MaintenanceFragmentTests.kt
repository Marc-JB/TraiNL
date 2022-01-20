package nl.marc_apps.ovgo.ui.maintenance

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import io.mockk.every
import io.mockk.mockk
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTrainInfoApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTravelInfoApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.ui.*
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.robolectric.annotation.Config
import retrofit2.Response
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
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
            start = Date(),
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

    @Test
    fun isMaintenanceItemShownWhenApiReturnsItem() {
        val testData = DutchRailwaysDisruption.DisruptionOrMaintenance(
            "abcd",
            DutchRailwaysDisruption.DisruptionType.MAINTENANCE,
            "MaIntEnANce",
            isActive = true,
            local = true,
            start = Date(),
            publicationSections = emptyList(),
            timespans = emptyList()
        )

        withKoinModule(createTestModule(Response.success(200, listOf(testData)))) {
            launchMaintenanceFragment(it).onFragment {
                val maintenanceList = it.view?.findViewById<RecyclerView>(R.id.list_maintenance)
                assertNotNull(maintenanceList)
                assertEquals(View.VISIBLE, maintenanceList.visibility)
                assertEquals(1, maintenanceList.adapter?.itemCount)

                val view = maintenanceList.findViewHolderForAdapterPosition(0)?.itemView
                assertEquals(testData.title, view?.findViewById<TextView>(R.id.label_title)?.text.toString())
            }
        }
    }
}
