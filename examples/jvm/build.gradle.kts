apply(plugin = "kotlin")

plugins {
    alias(libs.plugins.shadow)
    application
}

dependencies {
    implementation(project(":"))
    implementation(libs.ktor.client.cio)
}

application {
    mainClass.set("dev.s7a.example.gofile.Main")
}
