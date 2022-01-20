package nl.marc_apps.ovgo.ui

import android.app.Application
import android.content.Context
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import io.mockk.every
import io.mockk.mockk
import org.koin.core.module.Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

inline fun withKoinModule(module: Module, block: (Application) -> Unit) {
    val app: KoinTestApplication = ApplicationProvider.getApplicationContext()
    app.withKoinModule(module) {
        block(app)
    }
}

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

fun <T> mockCallWithResponse(response: Response<T>) = mockk<Call<T>> {
    val call = this
    every { enqueue(any()) } answers {
        arg<Callback<T>>(0).onResponse(call, response)
    }
}
