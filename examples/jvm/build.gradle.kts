plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("dev.s7a:gofile:1.0.0")
    implementation("io.ktor:ktor-client-cio:2.1.2")
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
