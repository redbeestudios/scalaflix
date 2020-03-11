/**
  * Frontend build commands.
  * Change these if you are using some other package manager. i.e: Yarn
  */
object FrontendCommands {
  val dependencyInstall: String = "npx install"
  val test: String = "npx run test"
  val serve: String = "npx run start"
  val build: String = "npx run build"
}