import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

apply(plugin = "kotlin")

plugins {
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

tasks.withType(KotlinJvmCompile::class.java).configureEach {
    kotlinOptions.apply {
        jvmTarget = "1.8"
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
