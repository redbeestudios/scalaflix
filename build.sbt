// scalastyle:ignore

import sbt._
import play.sbt.routes.RoutesKeys

organization in ThisBuild := "io.redbee"
scalaVersion in ThisBuild := "2.12.10"
version in ThisBuild := "0.0.1"

val playSettings = Seq(
  RoutesKeys.routesImport -= "controllers.Assets.Asset",
  RoutesKeys.generateReverseRouter := false
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-unchecked",
  "-Xfatal-warnings",
//  "-Xfuture",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
//  "-Yno-adapted-args",
//  "-Ypartial-unification",
//  "-Ywarn-unused-import",
  "-P:silencer:pathFilters=target/.*",
  s"-P:silencer:sourceRoots=${baseDirectory.value.getCanonicalPath}"
)
val scalaCompilerOptions = scalacOptions

// Libraries versions
val akkaVersion            = "2.6.3"
val circeVersion           = "0.12.2"
val containersVersion      = "1.12.0"
val dockerAccount          = "builder"
val playSlickVersion       = "4.0.2"

// Libraries sets
val akkaTyped = Seq(
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test withSources,
  "com.typesafe.akka" %% "akka-actor"               % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-slf4j"               % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-protobuf"            % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-testkit"             % akkaVersion % Test withSources,
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion % Test withSources,
  "com.typesafe.akka" %% "akka-stream-testkit"      % akkaVersion % Test withSources,
  "com.typesafe.akka" %% "akka-stream-typed"        % akkaVersion % Test withSources
)

val cats = Seq(
  "org.typelevel" %% "cats-core"   % "2.0.0",
  "org.typelevel" %% "cats-effect" % "2.0.0"
)

val circe = Seq(
  // Json
  "com.dripower" %% "play-circe"           % "2712.0",
  "io.circe"     %% "circe-core"           % circeVersion,
  "io.circe"     %% "circe-generic"        % circeVersion,
  "io.circe"     %% "circe-parser"         % circeVersion,
  "io.circe"     %% "circe-generic-extras" % circeVersion,
  "io.circe"     %% "circe-java8"          % "0.11.1"
)

val playLibs = Seq(
  ws,
  guice
)

val slick = Seq(
  "com.typesafe.play"  %% "play-slick"            % playSlickVersion withSources,
  "com.typesafe.play"  %% "play-slick-evolutions" % playSlickVersion withSources
)

val posgresql = Seq(
  "org.postgresql" % "postgresql" % "42.2.10"
)

val testLibs = Seq(
  "org.scalatest"           %% "scalatest"          % "3.1.1" % Test withSources,
  "org.mockito"             %  "mockito-core"       % "3.3.0" % Test withSources,
  "org.scalatestplus.play"  %% "scalatestplus-play" % "4.0.3" % Test withSources,
)

val logstash = Seq(
  "net.logstash.logback" % "logstash-logback-encoder" % "6.3" withSources
)

val minio = Seq(
  "io.minio" % "minio" % "6.0.13"
)

val xuggle = Seq(
  "xuggle" % "xuggle-xuggler" % "5.4"
)

// Common options
val commonSettings = {
  // Run checkstyle before compile
  lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

  Seq(
    // Auto-format code configurations
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    // Check Style
    compileScalastyle := scalastyle.in(Compile).toTask("").value,
    compileScalastyle := (compileScalastyle triggeredBy (scalafmt in Compile)).value,
    //
    scalacOptions ++= scalaCompilerOptions.value,
    //
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.sonatypeRepo("releases"),
      "Dcm4Che Repository" at "https://www.dcm4che.org/maven2"
    ),
    //
    parallelExecution in IntegrationTest := false,
    // Coverage configurations
    coverageExcludedPackages := "<empty>;router;global;controllers.circe.*;",
    coverageExcludedFiles := ".*HealthCheckController.*;.*Routes.*;.*Application;.*Loader",
    coverageMinimum := 50,
    coverageFailOnMinimum := true,
    ivyLoggingLevel := UpdateLogging.Quiet,
    javacOptions ++= Seq("-source", "1.8"),
    fork in run := true
  )
}

val streamingService        = "streaming"
val streamingServiceVersion = "0.0.1"

val metricsService        = "metrics"
val metricsServiceVersion = "0.0.1"

// root Project
lazy val scalaflix = (project in file("."))
  .aggregate(streaming, metrics)

// streaming Project
lazy val streaming = (project in file(s"services/$streamingService"))
  .enablePlugins(PlayScala, sbtdocker.DockerPlugin)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(filters) ++ akkaTyped ++ playLibs ++ testLibs ++ circe ++ logstash ++ minio
      ++ xuggle ++ slick ++ posgresql,
    playSettings,
    scalacOptions ++= scalaCompilerOptions.value,
    version := streamingServiceVersion,
    dockerSettings()
  )

// metrics Project
lazy val metrics = (project in file(s"services/$metricsService"))
  .enablePlugins(PlayScala, sbtdocker.DockerPlugin)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(filters) ++ akkaTyped ++ playLibs ++ testLibs ++ circe ++ logstash ++ slick
      ++ posgresql,
    playSettings,
    scalacOptions ++= scalaCompilerOptions.value,
    version := metricsServiceVersion,
    dockerSettings(),
    play.sbt.routes.RoutesKeys.routesImport ++= Seq(
      "models.FilmId",
      "java.time.LocalDateTime",
      "controllers.binders._"
    )
  )

// Global commands
Global / concurrentRestrictions := Seq(Tags.limitAll(200))
lazy val runAll = inputKey[Unit]("Runs all sub projects")
runAll := {
  (run in Compile in metrics).partialInput(" -Dplay.server.http.port=9090").evaluated
  (run in Compile in streaming).partialInput(" -Dplay.server.http.port=9000").evaluated
}

//
def dockerSettings(debugPort: Option[Int] = None) = Seq(
  // builder defaults options
  buildOptions in docker := BuildOptions(
    cache = false,
    removeIntermediateContainers = BuildOptions.Remove.Always,
    pullBaseImage = BuildOptions.Pull.Always
  ),
  // docker's image tagging
  imageNames in docker := Seq(
    ImageName(repository = name.value, tag = Some(version.value))
  ),
  // Dockerfile template
  dockerfile in docker := {
    val appDir: File       = stage.value
    val finder: PathFinder = (appDir / "conf") * "jce_policy-8.tar.gz"
    new Dockerfile {
      from("openjdk:8u242-jre")
      maintainer("redbee studios")
      workDir("/opt/docker")
      add(appDir, "/opt/docker")
      add(finder.get, "/usr/lib/jvm/default-jvm/jre/lib/security/")
      entryPoint("/opt/docker/bin/wrapper.sh")
    }
  }
)
