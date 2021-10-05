[![Android build](https://github.com/Marc-JB/OVgo/workflows/Android%20build/badge.svg)](https://github.com/Marc-JB/OVgo/actions) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Marc-JB_OVgo&metric=alert_status)](https://sonarcloud.io/dashboard?id=Marc-JB_OVgo) 
[![License: CC BY-NC-ND 4.0](https://badgen.net/badge/license/CC%20BY-NC-ND%204.0/blue)](https://creativecommons.org/licenses/by-nc-nd/4.0/) 
# TraiNL (previously "OVgo")
![Departures](/docs/screenshots/departures-1.png?raw=true "Departures (light mode)")
![Departures](/docs/screenshots/departures-2.png?raw=true "Departures (dark mode)")

Experimental public transport Android app that showcases various Android architecture patterns. The app is fully written in Kotlin and uses MVVM, dependency injection and various libraries for data access. 

## Components/libraries used
* [Android Jetpack](https://developer.android.com/jetpack)
  * Data
    * Room
    * Preferences DataStore
  * Lifecycle (with LiveData and ViewModels, used in MVVM)
  * Navigation
  * UI
    * AppCompat
    * Constraintlayout
    * Fragments
    * Material Design Components
    * Recyclerview
* Other
  * [Retrofit](https://square.github.io/retrofit/)
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
  * Serialization of JSON objects retrieved from the API using [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
  * Dependency Injection using [Koin](https://insert-koin.io/)

## Building & running this project
To build or run this project, both Firebase Crashlytics and a Dutch Railways API key are required. The setup is as follows:

### Setting up Firebase Crashlytics
1. Generate a `google-services.json` file from the [Firebase console](https://console.firebase.google.com/)
2. Copy the file to `/app/`

### Configuring the Dutch Railways API
1. Sign up to the [Dutch Railways developer portal](https://apiportal.ns.nl)
2. Subscribe to the `Ns-App` service
3. Copy your API key and use it in 
   - `local.properties` by adding a line `dutchRailways.travelInfoApi.key=` which is then followed by the API key.
   - the enviromental variables (in a CI/CD pipeline) by setting a property `DUTCHRAILWAYS_TRAVELINFOAPI_KEY` to your API key.