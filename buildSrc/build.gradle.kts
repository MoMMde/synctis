plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // https://git-scm.com/book/en/v2/Appendix-B%3A-Embedding-Git-in-your-Applications-JGit
    // https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit/6.9.0.202403050737-r
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")
}

// https://docs.gradle.org/8.4/release-notes.html#:~:text=Currently%2C%20you-,cannot,-run%20Gradle%20on
kotlin {
    jvmToolchain(17)
}