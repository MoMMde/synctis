import org.eclipse.jgit.api.Git
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)

    alias(libs.plugins.buildConfig)
}

group = "template.group"
version = "1.0"

dependencies {
    implementation(platform(libs.ktor.bom))

    implementation(libs.ktor.contentnegotiation.json)
    implementation(libs.ktor.contentnegotiation.server)

    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.logging)

    implementation(platform(libs.stdx.bom))
    implementation(libs.stdx.core)
    implementation(libs.stdx.envconf)
    implementation(libs.stdx.logging)
    implementation(libs.stdx.coroutines)

    implementation(libs.mongodb)

    implementation(libs.klogger)
    implementation(libs.slf4jSimple)
}

tasks {
    register("runWithEnv", RunWithEnvironmentConfig::class) {
        dependsOn("installDist")
        environmentFile.set(File(".env"))
    }

    kotlin {
        // https://docs.gradle.org/8.4/release-notes.html#:~:text=Currently%2C%20you-,cannot,-run%20Gradle%20on
        jvmToolchain(17)
    }

    jar {
        manifest {
            attributes["Main-Class"] = "$group.$name.${name.uppercaseFirstChar()}Kt"
        }
    }
}

// https://ktor.io/docs/server-packaging.html#run

val versionKey = "VERSION"
val gitShaKey = "GIT_SHA"
val gitBranchKey = "GIT_BRANCH"

buildConfig {
    val git = Git.open(project.rootDir.resolve(".git"))
    val head = git.repository.findRef("HEAD")
    buildConfigField("String?", gitShaKey, "\"${head.objectId.name}\"")
    buildConfigField("String?", gitBranchKey, "\"${git.repository.branch}\"")

    buildConfigField(String::class.java, versionKey, version.toString())
}