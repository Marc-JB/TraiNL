package nl.marc_apps.ovgo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.viewmodels.TripsViewModel

class TripsFragment : Fragment() {
    companion object {
        fun newInstance() = TripsFragment()
    }

    private val viewModel by viewModels<TripsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.trips_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
