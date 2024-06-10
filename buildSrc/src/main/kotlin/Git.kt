import io.matthewnelson.kmp.process.Process

const val githubShaKey = "GIT_SHA"
const val githubBranchKey = "GIT_BRANCH"

object Git {
    // todo: impl failure string when git could not be resolved
    val gitHashFromCommandline = Process.Builder(
        command = "git rev-parse HEAD"
    ).output().stdout
    val gitBranchFromCommandline = Process.Builder(
        command = "git rev-parse --abbrev-ref HEAD"
    ).output().stdout
}