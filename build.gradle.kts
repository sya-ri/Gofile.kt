import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("multiplatform") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("org.jetbrains.kotlinx.kover") version "0.7.4"
    id("org.jetbrains.dokka") version "1.9.10"
    id("org.jmailen.kotlinter") version "4.0.0"
    `maven-publish`
    signing
}

allprojects {
    apply(plugin = "org.jmailen.kotlinter")

    group = "dev.s7a"
    version = "1.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

kotlin {
    explicitApi()

    jvm()

    js {
        browser()
        nodejs()
    }

    // Android
    // androidNativeX86()
    // androidNativeX64()
    // androidNativeArm32()
    // androidNativeArm64()

    // Linux
    linuxArm64()
    linuxX64()

    // Windows
    mingwX64()

    // MacOS
    macosArm64()
    macosX64()

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()

    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    // watchosDeviceArm64()
    watchosX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("io.ktor:ktor-client-core:2.3.6")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:2.3.6")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
    }
}

tasks.withType(KotlinJvmCompile::class.java).configureEach {
    kotlinOptions.apply {
        jvmTarget = "1.8"
    }
}

tasks.withType<DokkaTask>().configureEach {
    val dokkaDir = projectDir.resolve("dokka")
    val version = version.toString()

    dependencies {
        dokkaPlugin("org.jetbrains.dokka:versioning-plugin:1.9.10")
    }
    outputDirectory.set(file(dokkaDir.resolve(version)))
    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.versioning.VersioningPlugin" to """
                {
                    "version": "$version",
                    "olderVersionsDir": "$dokkaDir"
                }
            """.trimIndent(),
        ),
    )
}

tasks.named("dokkaHtml") {
    val dokkaDir = projectDir.resolve("dokka")

    doFirst {
        dokkaDir.listFiles()?.forEach { file ->
            if (file != null && file.isDirectory && file.name.endsWith("-SNAPSHOT")) {
                file.deleteRecursively()
            }
        }
    }
    doLast {
        if (version.toString().endsWith("-SNAPSHOT").not()) {
            dokkaDir.resolve("index.html").writeText(
                """
                    <!DOCTYPE html>
                    <meta charset="utf-8">
                    <meta http-equiv="refresh" content="0; URL=./$version/">
                    <link rel="canonical" href="./$version/">
                """.trimIndent(),
            )
        }
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

// https://github.com/cashapp/zipline/blob/trunk/build.gradle.kts
// Don't attempt to sign anything if we don't have an in-memory key. Otherwise, the 'build' task
// triggers 'signJsPublication' even when we aren't publishing (and so don't have signing keys).
tasks.withType<Sign>().configureEach {
    enabled = project.findProperty("signingInMemoryKey") != null
}

publishing {
    repositories {
        maven {
            url = uri(
                if (version.toString().endsWith("SNAPSHOT")) {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                },
            )
            credentials {
                username = properties["sonatypeUsername"].toString()
                password = properties["sonatypePassword"].toString()
            }
        }
    }
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())
        pom {
            name.set("Gofile.kt")
            description.set("A kotlin wrapper for the Gofile.io API")
            url.set("https://github.com/sya-ri/Gofile.kt")

            licenses {
                license {
                    name.set("Apache License 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("sya-ri")
                    name.set("sya-ri")
                    email.set("contact@s7a.dev")
                }
            }
            scm {
                url.set("https://github.com/sya-ri/Gofile.kt")
            }
        }
    }
}

signing {
    val key = properties["signingKey"]?.toString()?.replace("\\n", "\n")
    val password = properties["signingPassword"]?.toString()

    useInMemoryPgpKeys(key, password)
    sign(publishing.publications)
}
