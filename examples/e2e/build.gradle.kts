plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

version = "1.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("dev.s7a:gofile:1.1.0-SNAPSHOT")
    implementation("io.ktor:ktor-client-cio:2.3.6")
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
