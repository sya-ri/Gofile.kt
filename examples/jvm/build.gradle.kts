apply(plugin = "kotlin")

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

dependencies {
    implementation(project(":"))
    implementation("io.ktor:ktor-client-cio:2.3.6")
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
