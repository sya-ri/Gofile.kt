plugins {
    kotlin("multiplatform") version "1.9.20"
}

kotlin {
    macosX64 {
        binaries {
            executable {
                entryPoint = "dev.s7a.example.gofile.main"
            }
        }
    }

    linuxX64 {
        binaries {
            executable {
                entryPoint = "dev.s7a.example.gofile.main"
            }
        }
    }

    mingwX64 {
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
            }
        }
        macosMain {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.3.6")
            }
        }
        linuxMain {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.3.6")
            }
        }
        mingwMain {
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:2.3.6")
            }
        }
    }
}
