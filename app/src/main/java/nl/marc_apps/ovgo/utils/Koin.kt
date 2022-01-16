package nl.marc_apps.ovgo.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition

inline fun <reified VM : ViewModel> Fragment.navGraphViewModel(
    @IdRes navGraphId: Int,
    noinline parameters: ParametersDefinition? = null
): Lazy<VM> {
    val backStackEntry: NavBackStackEntry by lazy { findNavController().getBackStackEntry(navGraphId) }
    return lazy(LazyThreadSafetyMode.NONE) {
        getViewModel(
            owner = { ViewModelOwner.from(backStackEntry as ViewModelStoreOwner, this as? SavedStateRegistryOwner) }, parameters = parameters
        )
    }
}
