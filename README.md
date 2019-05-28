# OVgo
Experimental public transport Android app that showcases various Android architecture patterns.

## To do
* Improve documentation
* Modularise project, either:
  * Using clean architecture
  * Using N-Layered architecture
  * Based on feature
  * A combination of those (e.g. clean architecture split up by feature, UI.Departures, UI.Disruptions, etc.)
* Cache data using [Room](https://developer.android.com/topic/libraries/architecture/room) for offline use
* Write [tests](https://developer.android.com/training/testing/)
  * Unit tests
* Add dependency injection using [Dagger](https://dagger.dev/)
* Migrate to stable versions of libraries
* Add travel feature
* Improve/expand existing features

## Implemented
* [Android Jetpack](https://developer.android.com/jetpack)
  * Foundation
    * Android KTX
    * AppCompat
  * [Architecture](https://developer.android.com/topic/libraries/architecture/)
    * Data Binding
    * Lifecycles
    * LiveData
    * ViewModel
  * UI
    * Fragment
* Other
  * [Retrofit](https://square.github.io/retrofit/) *(used with kotlin coroutines trough [a third-party library](https://github.com/gildor/kotlin-coroutines-retrofit))*
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
  * Serialization of JSON objects retrieved from the API using [Retrofit GSON converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)

## Screenshots of the app
![Trips](/docs/screenshots/trips-1.png?raw=true "Trips")
![Departures](/docs/screenshots/departures-1.png?raw=true "Departures")
![Departures](/docs/screenshots/departures-2.png?raw=true "Departures")
![Disruptions](/docs/screenshots/disruptions-1.png?raw=true "Disruptions")
![Disruptions](/docs/screenshots/disruptions-2.png?raw=true "Disruptions")
