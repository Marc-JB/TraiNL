package nl.marc_apps.ovgo.test_utils

import android.content.Context
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.internal.runner.junit4.statement.UiThreadStatement

fun <T : Fragment> T.withNavController(navController: NavController): T {
    viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
        if (viewLifecycleOwner != null) {
            Navigation.setViewNavController(requireView(), navController)
        }
    }
    return this
}

fun Context.createCustomTestNavController(
    @NavigationRes graphId: Int,
    customProperties: (TestNavHostController.() -> Unit)? = null
): TestNavHostController {
    val navController = TestNavHostController(this)
    navController.setViewModelStore(ViewModelStore())
    UiThreadStatement.runOnUiThread {
        navController.setGraph(graphId)
        customProperties?.invoke(navController)
    }
    return navController
}
