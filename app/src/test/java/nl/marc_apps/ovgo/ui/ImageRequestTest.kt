package nl.marc_apps.ovgo.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.request.ImageRequest
import nl.marc_apps.ovgo.test_utils.KoinTestApplication
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import kotlin.test.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class)
class ImageRequestTest {
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
    fun imageRequestShouldApplyUrlWhenCreated() {
        // Arrange
        val url = "https://www.example.com/"
        var imageRequest: ImageRequest? = null

        // Act
        composeTestRule.setContent {
            imageRequest = ImageRequest(url) {}
        }

        // Assert
        assertNotNull(imageRequest)
        assertEquals(url, imageRequest?.data)
    }
}
