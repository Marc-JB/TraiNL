<div align="center">

  ![Banner image](/docs/TraiNL-banner.webp?raw=true "Banner image")
  # TraiNL
  Public transport app that showcases various Android architecture patterns (like MVVM, Dependency Injection).

  [![Android Release](https://github.com/Marc-JB/TraiNL/actions/workflows/release.yml/badge.svg)](https://github.com/Marc-JB/TraiNL/actions) 
  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Marc-JB_OVgo&metric=alert_status)](https://sonarcloud.io/dashboard?id=Marc-JB_OVgo) 
  [![License: CC BY-NC-ND 4.0](https://badgen.net/badge/license/CC%20BY-NC-ND%204.0/blue)](https://creativecommons.org/licenses/by-nc-nd/4.0/) 

</div>

# :notebook_with_decorative_cover: Table of Contents
- [About the Project](#star2-about-the-project)
  * [Screenshots](#camera-screenshots)
  * [Tech Stack](#space_invader-tech-stack)
  * [Features](#dart-features)
  * [Color Reference](#art-color-reference)
  * [Environment Variables](#key-environment-variables)
- [Getting Started](#toolbox-getting-started)
  * [Prerequisites](#bangbang-prerequisites)
  * [Installation](#gear-installation)
  * [Running Tests](#test_tube-running-tests)
  * [Deployment](#triangular_flag_on_post-deployment)
- [Usage](#eyes-usage)
- [License](#warning-license)
- [Acknowledgements](#gem-acknowledgements)

## :star2: About the Project
### :camera: Screenshots
![Screenshot: Departures (light mode)](/docs/screenshots/departures-1.webp?raw=true "Screenshot: Departures (light mode)")
![Screenshot: Departures (dark mode)](/docs/screenshots/departures-2.webp?raw=true "Screenshot: Departures (dark mode)") 

### :space_invader: Tech Stack
An Android application in Kotlin with the following libraries/patterns:
* [Android Jetpack](https://developer.android.com/jetpack)
  * Data
    * Room
    * Preferences DataStore
  * Lifecycle ViewModels
  * Navigation
  * UI
    * Jetpack Compose
    * Fragments
    * Material Design Components
* Other
  * [Retrofit](https://square.github.io/retrofit/)
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) (Kotlin Flow is also used)
  * Serialization of JSON objects retrieved from the API using [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
  * Dependency Injection using [Koin](https://insert-koin.io/)

### :dart: Features
* Viewing train departures at any Dutch train station
* Viewing details of a train, like toilet availability or delay per station on the route
* Viewing a list of disruptions (including maintenance)
* Improved reliability of information shown in the app when compared to other providers that use the Dutch Railways API

### :art: Color Reference
| Color | Hex |
| --- | --- |
| Primary Color | ![#1976D2](https://via.placeholder.com/16/1976D2.webp?text=+) #1976D2 |
| Primary Color (Light) | ![#63A4FF](https://via.placeholder.com/16/63A4FF.webp?text=+) #63A4FF |
| Primary Color (Dark) | ![#004BA0](https://via.placeholder.com/16/004BA0.webp?text=+) #004BA0 |
| Secondary Color | ![#FFEB3B](https://via.placeholder.com/16/FFEB3B.webp?text=+) #FFEB3B |
| Secondary Color (Light) | ![#FFFF72](https://via.placeholder.com/16/FFFF72.webp?text=+) #FFFF72 |
| Secondary Color (Dark) | ![#C8B900](https://via.placeholder.com/16/C8B900.webp?text=+) #C8B900 |
| App Icon Color (Start) | ![#536DFE](https://via.placeholder.com/16/536DFE.webp?text=+) #536DFE |
| App Icon Color (End) | ![#6A3DE8](https://via.placeholder.com/16/6A3DE8.webp?text=+) #6A3DE8 |

### :key: Environment Variables
This project requires a Firebase Crashlytics configuration file and a Dutch Railways API key.

**Setting up Firebase Crashlytics**
1. Generate a `google-services.json` file from the [Firebase console](https://console.firebase.google.com/)
2. Copy the file to `/app/`

**Configuring the Dutch Railways API**
1. Sign up to the [Dutch Railways developer portal](https://apiportal.ns.nl)
2. Subscribe to the `Ns-App` service
3. Copy your API key and use it in 
   - `local.properties` by adding a line `dutchRailways.travelInfoApi.key=` which is then followed by the API key.
   - the enviromental variables (in a CI/CD pipeline) by setting a property `DUTCHRAILWAYS_TRAVELINFOAPI_KEY` to your API key.

## 	:toolbox: Getting Started
*Note: This project may contain references to "OVgo". This was the previous name for this app.*

### :bangbang: Prerequisites
This project uses Gradle as build tool. Android Studio is the recommended IDE to deploy or test this application.

### :gear: Installation
To build the project from a command line/terminal, run the following command:
```bash
./gradlew build
```

### :test_tube: Running Tests
To run tests, run:
```bash
./gradlew :app:cleanTestDebugUnitTest :app:testDebugUnitTest
```

### :triangular_flag_on_post: Deployment
To build `.apk` and `.aar` files, run:
```bash
./gradlew bundleRelease assembleRelease
```

## :eyes: Usage
New versions of this app are uploaded to the [releases page](https://github.com/Marc-JB/TraiNL/releases/). You can download an `.apk` file there to install on your Android device. 

## :warning: License
This project is published under the Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0) License. Read more about this license in the `LICENSE` file or on [https://creativecommons.org/licenses/by-nc-nd/4.0/](https://creativecommons.org/licenses/by-nc-nd/4.0/). 

## :gem: Acknowledgements
- [Awesome Readme Template](https://github.com/Louis3797/awesome-readme-template)
- [Icon Kitchen](https://icon.kitchen/)
