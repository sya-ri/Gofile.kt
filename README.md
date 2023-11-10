<h1 align="center">
  <img src="https://gofile.io/dist/img/logo-small-70.png" alt="GoFile logo" width="80rem" />
  Gofile.kt
</h1>

<span align="center">

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/dev.s7a/gofile)](https://search.maven.org/artifact/dev.s7a/gofile)
[![KDoc link](https://img.shields.io/badge/API_reference-KDoc-blue)](https://gh.s7a.dev/Gofile.kt)
[![Build status](https://img.shields.io/github/actions/workflow/status/sya-ri/Gofile.kt/build.yml?branch=master&label=Test&logo=github)](.github/workflows/build.yml)

A kotlin wrapper for the [Gofile.io](https://gofile.io) API.

</span>

## Features

- Support multiplatform.
  - [x] JVM
  - [x] JS
  - [x] Native
    - linuxArm64
    - linuxX64
    - mingwX64
    - macosArm64
    - macosX64
    - iosArm64
    - iosSimulatorArm64
    - iosX64
    - tvosArm64
    - tvosSimulatorArm64
    - tvosX64
    - watchosArm32
    - watchosArm64
    - watchosSimulatorArm64
    - watchosX64
- Supports all endpoints. (API Version: 2023-04-20)
  - [x] getServer
  - [x] uploadFile
  - [x] getContent
  - [x] createFolder
  - [x] setOption
  - [x] copyContent
  - [x] deleteContent
  - [x] getAccountDetails
- Support all account tiers.
  - [x] Guest (without token)
  - [x] Standard
  - [x] Donor
- Error handling using [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/).
- Asynchronous with [Ktor](https://ktor.io).

## Installation

### Add dependency

You also need to add Ktor client engine.

#### build.gradle.kts

```kt
repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.s7a:gofile:1.0.0")
    // See also: https://ktor.io/docs/http-client-engines.html
    // implementation("io.ktor:ktor-client-???:$ktor_version")
}
```

#### build.gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "dev.s7a:gofile:1.0.0"
    // See also: https://ktor.io/docs/http-client-engines.html
    // implementation "io.ktor:ktor-client-???:$ktor_version"
}
```

### Main.kt

```kt
import dev.s7a.gofile.GofileClient

fun main() {
    val client = GofileClient() // Automatically find the engine
    // client.uploadFile(), etc...
}
```

## Documentation

- [API Document](https://gh.s7a.dev/Gofile.kt)
- [Examples](examples)
- [CONTRIBUTING.md](CONTRIBUTING.md)
