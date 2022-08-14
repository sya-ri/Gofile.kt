plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("dev.s7a:gofile:1.0.0-SNAPSHOT")
    implementation("io.ktor:ktor-client-cio:2.1.0")
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
