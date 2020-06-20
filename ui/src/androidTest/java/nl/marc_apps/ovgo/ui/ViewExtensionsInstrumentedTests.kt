package nl.marc_apps.ovgo.ui

import android.widget.ImageView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewExtensionsInstrumentedTests {
    @Test
    fun imageListLoadImagesRemovesAllViewsInLayoutFirst() {
        // Arrange
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val layout = ImageList(appContext)
        val child = ImageView(appContext)
        layout.addView(child)

        // Act
        loadImages(layout, emptyList())

        // Assert
        assertEquals(0, layout.childCount)
    }
}
