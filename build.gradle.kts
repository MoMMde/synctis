import org.eclipse.jgit.api.Git

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

    testImplementation(libs.ktor.server.testHost)
    testImplementation(libs.ktor.client.contentnegotiation)

    testImplementation(libs.kotlinTest)
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

    test {
        useJUnitPlatform()
    }
}

application {
    mainClass.set("template.group.name.TemplateKt")
}

// https://ktor.io/docs/server-packaging.html#run

val versionKey = "VERSION"
val gitShaKey = "GIT_SHA"
val gitBranchKey = "GIT_BRANCH"

buildConfig {
    packageName("${project.group}.${project.name}.generated")

    val git = Git.open(project.rootDir.resolve(".git"))
    val head = git.repository.findRef("HEAD")
    buildConfigField("String?", gitShaKey, "\"${head.objectId.name}\"")
    buildConfigField("String?", gitBranchKey, "\"${git.repository.branch}\"")

    buildConfigField(String::class.java, versionKey, version.toString())
}