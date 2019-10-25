# OVgo
Experimental public transport Android app that showcases various Android architecture patterns.

## To do
* Cache data using [Room](https://developer.android.com/topic/libraries/architecture/room) for offline use
* Write [tests](https://developer.android.com/training/testing/)
  * Unit tests
* Use the stable version of the Material Design library
* Add travel feature
* Improve/expand existing features
* Improve documentation & architecture
* Migrate to Android Jetpack Dependency Injection as soon as it's available

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
  * [Retrofit](https://square.github.io/retrofit/)
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
  * Serialization of JSON objects retrieved from the API using [Retrofit GSON converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)
  * Clean architecture
  *note: there's a dependency between the UI (app) module and the API module needed for dependency injection*
  * Manual Dependecy Injection

## Screenshots of the app
![Trips](/docs/screenshots/trips-1.png?raw=true "Trips")
![Departures](/docs/screenshots/departures-1.png?raw=true "Departures")
![Departures](/docs/screenshots/departures-2.png?raw=true "Departures")
![Disruptions](/docs/screenshots/disruptions-1.png?raw=true "Disruptions")
![Disruptions](/docs/screenshots/disruptions-2.png?raw=true "Disruptions")
