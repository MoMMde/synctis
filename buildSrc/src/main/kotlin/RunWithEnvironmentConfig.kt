import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.Sync
import java.io.File
import java.util.regex.Pattern

abstract class RunWithEnvironmentConfig : JavaExec() {
    @get:InputFile
    abstract val environmentFile: Property<File>

    init {
        mainClass.set("xyz.mommde.synctis.SynctisKt")
    }

    override fun exec() {
        val syncTask = (project.getTasksByName("installDist", false).first() as Sync)
        val classPath = syncTask
            .destinationDir
            .resolve("lib")
            .listFiles()
            ?.map { it.toPath() }

        copyEnvironment()
        if(environment.getOrDefault("DEBUG", "FALSE").equals("TRUE")) {
            jvmArgs("-Dorg.slf4j.simpleLogger.defaultLogLevel=debug")
        }
        classpath(classPath)
        super.exec()
    }

    private fun copyEnvironment() {
        val envFile = environmentFile.get()
        if (!envFile.exists()) {
            error("Could not find environment file: ${envFile.absolutePath}")
        }

        readEnvFile(envFile) { key, value ->
            environment(key, value)
        }
    }
}

fun readEnvFile(file: File, invokeable: (String, String) -> Unit) {
    file.readLines()
        .filter { it.isNotEmpty() && !it.startsWith("#") }.forEach { line ->
            val (key, value) = line.split(Pattern.compile("="), 2)
            invokeable(key, value)
        }
}