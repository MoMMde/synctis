import com.bmuschko.gradle.docker.tasks.DockerVersion
import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.github.dockerjava.core.DockerContextMetaFile.Endpoints.Docker

plugins {
    `kotlin-dsl`

    id("com.bmuschko.docker-remote-api") version "9.4.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/05nelsonm/kmp-process
    implementation("io.matthewnelson.kmp-process:process:0.1.0-alpha03")
    // https://git-scm.com/book/en/v2/Appendix-B%3A-Embedding-Git-in-your-Applications-JGit
    // https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit/6.9.0.202403050737-r
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")
}

// https://docs.gradle.org/8.4/release-notes.html#:~:text=Currently%2C%20you-,cannot,-run%20Gradle%20on
kotlin {
    jvmToolchain(17)
}

tasks {
    create("runMongoDB", DockerCreateContainer::class) {

    }
}