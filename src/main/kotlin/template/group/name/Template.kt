package template.group.name

fun main() {
    if (Config.DEBUG)
        println("Debug enabled")
    runKtor(connectToMongoDB())
}