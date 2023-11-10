import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinter)
    `maven-publish`
    signing
}

allprojects {
    apply(plugin = "org.jmailen.kotlinter")

    group = "dev.s7a"
    version = "1.1.0"

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
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
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
        dokkaPlugin(libs.dokka.plugin.versioning)
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
