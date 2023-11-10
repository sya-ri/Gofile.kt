plugins {
    kotlin("multiplatform") version "1.9.20"
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")

    when {
        hostOs == "Mac OS X" -> macosX64()
        hostOs == "Linux" -> linuxX64()
        isMingwX64 -> mingwX64()
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }.apply {
        binaries {
            executable {
                entryPoint = "dev.s7a.example.gofile.main"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":"))
                implementation("com.squareup.okio:okio:3.2.0")
                if (isMingwX64) {
                    implementation("io.ktor:ktor-client-winhttp:2.3.6")
                } else {
                    implementation("io.ktor:ktor-client-cio:2.3.6")
                }
            }
        }
    }
}
