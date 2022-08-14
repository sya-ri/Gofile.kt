plugins {
    kotlin("multiplatform") version "1.7.10"
}

group = "dev.s7a.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "dev.s7a.example.gofile.main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("dev.s7a:gofile:1.0.0-SNAPSHOT")
                implementation("io.ktor:ktor-client-cio:2.1.0")
                implementation("com.squareup.okio:okio:3.2.0")
            }
        }
        val nativeTest by getting
    }
}
