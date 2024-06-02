import io.matthewnelson.kmp.process.Process
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.regex.Pattern

abstract class RunWithEnvironmentConfig : DefaultTask() {
    @get:InputFile
    abstract val environmentFile: Property<File>

    private val processBuilder = Process.Builder(
        command = project
            .layout
            .buildDirectory
            .get()
            .dir("install")
            .dir("worcester")
            .dir("bin")
            .file("worcester")
            .asFile
            .absolutePath
    )


    @TaskAction
    fun executeBinary() {

        processBuilder.copyEnvironment()

        println("Running with command: ${processBuilder.command}")

        processBuilder.output {
            maxBuffer = 1024
            timeoutMillis = 500
        }.let { output ->
            println("-- installDist Execute Task (STDOUT) --")
            println(output.stdout)
            println("-- installDist Execute Task (STDERR) --")
            println(output.stderr)
        }
    }

    private fun Process.Builder.copyEnvironment() {
        val envFile = environmentFile.get()
        if (!envFile.exists()) {
            error("Could not find environment file: ${envFile.absolutePath}")
        }

        val content = envFile.readLines()

        content.filter { it.isNotEmpty() && !it.startsWith("#") }.forEach { line ->
            val (key, value) = line.split(Pattern.compile("="), 2)
            this.environment(key, value)
        }
    }
}