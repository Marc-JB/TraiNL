package nl.marc_apps.ovgo.ui.disruptions

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
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
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class)
class DisruptionsFragmentTests {
    private fun createTestModule(response: Response<List<DutchRailwaysDisruption>>): Module {
        return module {
            single {
                mockk<DutchRailwaysTravelInfoApi> {
                    val types = listOf(
                        DutchRailwaysDisruption.DisruptionType.CALAMITY,
                        DutchRailwaysDisruption.DisruptionType.DISRUPTION
                    )

                    every {
                        getDisruptions(any(), any(), types.joinToString(separator = ",") { it.name }, any())
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
                DisruptionsViewModel(get())
            }
        }
    }

    private fun launchDisruptionsFragment(context: Context): FragmentScenario<DisruptionsFragment> {
        val testNavHostController = context.createCustomTestNavController(R.navigation.main_navigation) {
            setCurrentDestination(R.id.disruptions)
        }

        return launchFragmentInContainer(themeResId = R.style.Theme_OVgo) {
            DisruptionsFragment().withNavController(testNavHostController)
        }
    }

    @Test
    fun isPlaceholderShownWhenDisruptionsListIsEmpty() {
        withKoinModule(createTestModule(Response.success(200, emptyList()))) {
            launchDisruptionsFragment(it).onFragment {
                val placeholder = it.view?.findViewById<View>(R.id.partial_image_with_label_placeholder)
                assertNotNull(placeholder)
                assertEquals(View.VISIBLE, placeholder.visibility)
            }
        }
    }

    @Test
    fun isPlaceholderHiddenWhenDisruptionsListIsNotEmpty() {
        val testData = DutchRailwaysDisruption.DisruptionOrMaintenance(
            "abcd",
            DutchRailwaysDisruption.DisruptionType.DISRUPTION,
            "DisRupTIoN",
            isActive = true,
            local = true,
            start = Date(),
            publicationSections = emptyList(),
            timespans = emptyList()
        )

        withKoinModule(createTestModule(Response.success(200, listOf(testData)))) {
            launchDisruptionsFragment(it).onFragment {
                val placeholder = it.view?.findViewById<View>(R.id.partial_image_with_label_placeholder)
                assertNotNull(placeholder)
                assertEquals(View.GONE, placeholder.visibility)
            }
        }
    }

    @Test
    fun isDisruptionItemShownWhenApiReturnsItem() {
        val testData = DutchRailwaysDisruption.DisruptionOrMaintenance(
            "abcd",
            DutchRailwaysDisruption.DisruptionType.DISRUPTION,
            "DisRupTIoN",
            isActive = true,
            local = true,
            start = Date(),
            publicationSections = emptyList(),
            timespans = emptyList()
        )

        withKoinModule(createTestModule(Response.success(200, listOf(testData)))) {
            launchDisruptionsFragment(it).onFragment {
                val disruptionList = it.view?.findViewById<RecyclerView>(R.id.list_disruptions)
                assertNotNull(disruptionList)
                assertEquals(View.VISIBLE, disruptionList.visibility)
                assertEquals(1, disruptionList.adapter?.itemCount)

                val view = disruptionList.findViewHolderForAdapterPosition(0)?.itemView
                assertEquals(testData.title, view?.findViewById<TextView>(R.id.label_title)?.text.toString())
            }
        }
    }
}
