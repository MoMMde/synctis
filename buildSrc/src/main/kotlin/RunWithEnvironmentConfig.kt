import io.matthewnelson.kmp.process.Process
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.io.File
import java.util.regex.Pattern

abstract class RunWithEnvironmentConfig : DefaultTask() {
    @get:InputFile
    abstract val environmentFile: Property<File>

    private val processBuilder = Process.Builder(
        // https://github.com/gradle/gradle/blob/master/subprojects/core/src/main/java/org/gradle/api/tasks/Sync.java#L103C17-L103C34
        // will return the output dir of the installDist gradle task that is beeing implemented by the ktor plugin
        command = (project.getTasksByName("installDist", false).first() as Sync)
            .destinationDir
            .resolve("bin")
            .resolve(project.name)
            .absolutePath
    )


    @TaskAction
    fun executeBinary() {
        processBuilder.copyEnvironment()

        println("executing file: ${processBuilder.command}")

        processBuilder.output {
            maxBuffer = 1 // 1 KiB
            timeoutMillis = 2147483647
        }.let { output ->
            println("-- installDist Task (STDOUT) --")
            println(output.stdout)
            println("-- installDist Task (STDERR) --")
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