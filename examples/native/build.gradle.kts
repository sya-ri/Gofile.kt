plugins {
    alias(libs.plugins.kotlin.multiplatform)
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
                freeCompilerArgs += "-Xdisable-phases=EscapeAnalysis"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":"))
                implementation(libs.okio)
                if (isMingwX64) {
                    implementation(libs.ktor.client.winhttp)
                } else {
                    implementation(libs.ktor.client.cio)
                }
            }
        }
    }
}
