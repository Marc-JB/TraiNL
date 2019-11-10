package nl.marc_apps.ovgo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val dataRepository: PublicTransportDataRepository by inject()

    private val disruptionCount: MutableLiveData<Int> = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: handle more nicely
        CoroutineScope(Dispatchers.IO).launch {
            disruptionCount.postValue(dataRepository.getDisruptions(true).size)
        }

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        disruptionCount.observe(this@MainActivity) {
            navView.getOrCreateBadge(R.id.navigation_disruptions).number = it
        }
    }
}
