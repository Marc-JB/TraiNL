package nl.marc_apps.ovgo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import nl.marc_apps.ovgo.ui.databinding.ActivityMainBinding
import nl.marc_apps.ovgo.ui.extensions.KeepStateNavigator
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.default_navigation)

        binding.navView.setupWithNavController(navController)

        viewModel.disruptionCount.observe(this) {
            if (it != -1) binding.navView.getOrCreateBadge(R.id.navigation_disruptions).number = it
        }
    }
}
