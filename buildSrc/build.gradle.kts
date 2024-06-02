plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/05nelsonm/kmp-process
    implementation("io.matthewnelson.kmp-process:process:0.1.0-alpha03")
}

// https://docs.gradle.org/8.4/release-notes.html#:~:text=Currently%2C%20you-,cannot,-run%20Gradle%20on
kotlin {
    jvmToolchain(20)
}