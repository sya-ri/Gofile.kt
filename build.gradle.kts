import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.kotlinx.kover") version "0.6.0-Beta"
    id("org.jetbrains.dokka") version "1.7.10"
    `maven-publish`
}

group = "dev.s7a"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.1.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.1.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:2.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.withType<DokkaTask>().configureEach {
    val dokkaDir = projectDir.resolve("dokka")
    val version = version.toString()

    dependencies {
        dokkaPlugin("org.jetbrains.dokka:versioning-plugin:1.7.10")
    }
    outputDirectory.set(file(dokkaDir.resolve(version)))
    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.versioning.VersioningPlugin" to """
                {
                    "version": "$version",
                    "olderVersionsDir": "$dokkaDir"
                }
            """.trimIndent()
        )
    )
}

tasks.named("dokkaHtml") {
    val dokkaDir = projectDir.resolve("dokka")

    doLast {
        dokkaDir.resolve("index.html").writeText(
            """
                <!DOCTYPE html>
                <meta charset="utf-8">
                <meta http-equiv="refresh" content="0; URL=./$version/">
                <link rel="canonical" href="./$version/">
            """.trimIndent()
        )
    }
}
