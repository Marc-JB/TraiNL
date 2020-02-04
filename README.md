# OVgo [![Java CI](https://github.com/Marc-JB/OVgo/workflows/Java%20CI/badge.svg)](https://github.com/Marc-JB/OVgo/actions?query=workflow%3A%22Java+CI%22)
Experimental public transport Android app that showcases various Android architecture patterns.

## To do
* Cache data using [Room](https://developer.android.com/topic/libraries/architecture/room) for offline use
* Write [tests](https://developer.android.com/training/testing/)
  * Unit tests
* Add travel feature
* Improve/expand existing features
* Improve documentation & architecture

## Implemented
* [Android Jetpack](https://developer.android.com/jetpack)
  * Foundation
    * Android KTX
    * AppCompat
  * [Architecture](https://developer.android.com/topic/libraries/architecture/)
    * Data Binding
    * Lifecycles
    * LiveData
    * Navigation
    * ViewModel
  * UI
    * Fragment
    * Layout
* Other
  * [Retrofit](https://square.github.io/retrofit/)
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
  * Serialization of JSON objects retrieved from the API using [Retrofit GSON converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)
  * Clean architecture
  * Dependecy Injection using [Koin](https://insert-koin.io/)

## Screenshots of the app
![Trips](/docs/screenshots/trips-1.png?raw=true "Trips")
![Departures](/docs/screenshots/departures-1.png?raw=true "Departures")
![Departures](/docs/screenshots/departures-2.png?raw=true "Departures")
![Disruptions](/docs/screenshots/disruptions-1.png?raw=true "Disruptions")
![Disruptions](/docs/screenshots/disruptions-2.png?raw=true "Disruptions")
