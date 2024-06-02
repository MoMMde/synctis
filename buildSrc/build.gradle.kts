plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    // https://github.com/05nelsonm/kmp-process
    implementation("io.matthewnelson.kmp-process:process:0.1.0-alpha03")

    implementation("io.ktor.plugin:plugin:3.0.0-beta-1")
}

// https://docs.gradle.org/8.4/release-notes.html#:~:text=Currently%2C%20you-,cannot,-run%20Gradle%20on
kotlin {
    jvmToolchain(17)
}