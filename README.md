[![Android tests](https://github.com/Marc-JB/OVgo/workflows/Android%20tests/badge.svg)](https://github.com/Marc-JB/OVgo/actions)
[![Android build](https://github.com/Marc-JB/OVgo/workflows/Android%20build/badge.svg)](https://github.com/Marc-JB/OVgo/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Marc-JB_OVgo&metric=alert_status)](https://sonarcloud.io/dashboard?id=Marc-JB_OVgo)
[![License: CC BY-NC-ND 4.0](https://badgen.net/badge/license/CC%20BY-NC-ND%204.0/blue)](https://creativecommons.org/licenses/by-nc-nd/4.0/)
# OVgo
Experimental public transport Android app that showcases various Android architecture patterns.

## Implemented
* [Android Jetpack](https://developer.android.com/jetpack)
  * Foundation
    * Android KTX
    * AppCompat
  * [Architecture](https://developer.android.com/topic/libraries/architecture/)
    * View Binding
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
  * Dependency Injection using [Koin](https://insert-koin.io/)

## Screenshots of the app
![Trips](/docs/screenshots/trips-1.png?raw=true "Trips")
![Departures](/docs/screenshots/departures-1.png?raw=true "Departures")
![Departures](/docs/screenshots/departures-2.png?raw=true "Departures")
![Disruptions](/docs/screenshots/disruptions-1.png?raw=true "Disruptions")
![Disruptions](/docs/screenshots/disruptions-2.png?raw=true "Disruptions")
