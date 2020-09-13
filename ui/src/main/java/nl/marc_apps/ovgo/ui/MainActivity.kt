package nl.marc_apps.ovgo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import nl.marc_apps.ovgo.ui.extensions.KeepStateNavigator
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.default_navigation)

        navView.setupWithNavController(navController)

        viewModel.disruptionCount.observe(this) {
            if (it != -1) navView.getOrCreateBadge(R.id.navigation_disruptions).number = it
        }
    }
}
