package nl.marc_apps.ovgo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.ui.departures.DeparturesFragment
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsFragment
import nl.marc_apps.ovgo.ui.trips.TripsFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val dataRepository: PublicTransportDataRepository by inject()

    private lateinit var tripsFragment: Fragment
    private lateinit var departuresFragment: Fragment
    private lateinit var disruptionsFragment: Fragment

    private val disruptionCount: MutableLiveData<Int> = MutableLiveData(0)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_trips -> switchToFragment(tripsFragment)
            R.id.navigation_departures -> switchToFragment(departuresFragment)
            R.id.navigation_disruptions -> switchToFragment(disruptionsFragment)
            else -> return@OnNavigationItemSelectedListener false
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: handle more nicely
        CoroutineScope(Dispatchers.IO).launch {
            disruptionCount.postValue(dataRepository.getDisruptions(true).size)
        }

        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.nav_view).apply {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            disruptionCount.observe(this@MainActivity) {
                getOrCreateBadge(R.id.navigation_disruptions).number = it
            }
        }

        when(val frag: Fragment? = supportFragmentManager.findFragmentByTag("fragment")){
            is DeparturesFragment -> loadFragments(departures = frag)
            is TripsFragment -> loadFragments(trips = frag)
            is DisruptionsFragment -> loadFragments(disruptions = frag)
            else -> loadFragments()
        }

        if(savedInstanceState == null) switchToFragment(tripsFragment)
    }

    private fun loadFragments(
        trips: Fragment = TripsFragment(),
        departures: Fragment = DeparturesFragment(),
        disruptions: Fragment = DisruptionsFragment()
    ) {
        tripsFragment = trips
        departuresFragment = departures
        disruptionsFragment = disruptions
    }

    private fun switchToFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_holder, fragment, "fragment").commit()
}
