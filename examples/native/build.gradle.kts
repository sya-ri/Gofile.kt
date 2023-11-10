plugins {
    kotlin("multiplatform") version "1.9.20"
}

version = "1.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
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
                implementation("dev.s7a:gofile:1.1.0-SNAPSHOT")
                implementation("com.squareup.okio:okio:3.2.0")
            }
        }
        mingwMain {
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:2.3.6")
            }
        }
    }
}
