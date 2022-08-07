plugins {
    kotlin("jvm") version "1.7.10"
    application
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
