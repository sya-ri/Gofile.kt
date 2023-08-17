import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.jetbrains.kotlinx.kover") version "0.7.3"
    id("org.jetbrains.dokka") version "1.8.20"
    `maven-publish`
    signing
}

group = "dev.s7a"
version = "1.0.1-SNAPSHOT"

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
                implementation("io.ktor:ktor-client-core:2.2.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:2.2.4")
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
        dokkaPlugin("org.jetbrains.dokka:versioning-plugin:1.8.20")
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
                """.trimIndent()
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
                }
            )
            credentials {
                username = project.properties["credentials.username"].toString()
                password = project.properties["credentials.password"].toString()
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
    sign(publishing.publications)
}
