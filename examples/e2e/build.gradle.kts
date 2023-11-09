plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

version = "1.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("dev.s7a:gofile:1.0.1-SNAPSHOT")
    implementation("io.ktor:ktor-client-cio:2.1.3")
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
