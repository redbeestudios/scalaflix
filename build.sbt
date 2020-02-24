// scalastyle:ignore
import sbt.IntegrationTest
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
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
  "-Xfuture",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Yno-adapted-args",
  "-Ypartial-unification",
  "-Ywarn-unused-import",
  "-P:silencer:pathFilters=target/.*",
  s"-P:silencer:sourceRoots=${baseDirectory.value.getCanonicalPath}"
)
val scalaCompilerOptions = scalacOptions

// Libraries versions
val akkaVersion            = "2.6.0"
val akkaStreamKafkaVersion = "1.1.0"
val circeVersion           = "0.12.2"
val containersVersion      = "1.12.0"
val dockerAccount          = "builder"
val playSlickVersion       = "4.0.2"

// Libraries sets
val akkaTyped = Seq(
  "com.typesafe.akka" %% "akka-actor-typed"          % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-actor-testkit-typed"  % akkaVersion % "test" withSources,
  "com.typesafe.akka" %% "akka-actor"                % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-slf4j"                % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-protobuf"             % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-stream"               % akkaVersion withSources,
  "com.typesafe.akka" %% "akka-stream-kafka"         % akkaStreamKafkaVersion withSources,
  "com.typesafe.akka" %% "akka-stream-kafka-testkit" % akkaStreamKafkaVersion withSources,
  "com.typesafe.akka" %% "akka-testkit"              % akkaVersion % "test" withSources,
  "com.typesafe.akka" %% "akka-stream-testkit"       % akkaVersion % "test" withSources
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
  "io.circe"     %% "circe-java8"          % "0.11.1",
  "io.taig"      %% "circe-validation"     % "0.1.1"
)

val playLibs = Seq(
  guice,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test" withSources,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % "test",
  "com.typesafe.play" %% "play" % "2.7.3" withSources
)

val slick = Seq(
  "com.typesafe.slick" %% "slick"                 % "3.3.1" withSources,
  "com.typesafe.slick" %% "slick-hikaricp"        % "3.3.1" withSources,
  "com.typesafe.play"  %% "play-slick"            % playSlickVersion withSources,
  "com.typesafe.play"  %% "play-slick-evolutions" % playSlickVersion withSources
)

val testLibs = Seq(
  "org.scalatest" %% "scalatest"   % "3.0.5" % "test" withSources,
  "org.mockito"   % "mockito-core" % "3.0.0" % "test" withSources
)

val logstash = Seq(
  "net.logstash.logback" % "logstash-logback-encoder" % "6.2" withSources,
  "ch.qos.logback" % "logback-core" % "1.2.3" withSources,
  "ch.qos.logback" % "logback-classic" % "1.2.3" withSources
)

// Common options
val commonSettings = Seq(
  // Auto-format code configurations
  scalafmtOnCompile := true,
  scalafmtTestOnCompile := true,
  //
  scalacOptions ++= scalaCompilerOptions.value,
  //
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  //
  parallelExecution in IntegrationTest := false,
  // Coverage configurations
  coverageExcludedPackages := "<empty>;router;global;controllers.circe.*;",
  coverageExcludedFiles := ".*BuildInfo.*;.*HealthCheckController.*;.*Routes.*;.*Application;.*Loader",
  coverageMinimum := 50,
  coverageFailOnMinimum := true,
  // Scala doc
  autoAPIMappings := true,
  exportJars := true
)

val streamingService        = "streaming"
val streamingServiceVersion = "0.0.1"

val metricsService        = "metrics"
val metricsServiceVersion = "0.0.1"


// root Project
lazy val scalaflix = (project in file("."))
  .aggregate(`streaming`, `metrics`)

// streaming Project
lazy val `streaming` = (project in file(s"services/$streamingService"))
  .enablePlugins(PlayScala, sbtdocker.DockerPlugin, BuildInfoPlugin, JavaAppPackaging)
  .settings(
    commonSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, BuildInfoKey.action("build_time") {
      java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.now())
    }),
    buildInfoPackage := "controllers",
    buildInfoObject := "BuildInfo",
    buildInfoOptions += BuildInfoOption.ToJson,
    libraryDependencies ++= Seq(filters) ++ akkaTyped ++ playLibs ++ testLibs ++ logstash,
    playSettings,
    scalacOptions ++= scalaCompilerOptions.value,
    version := streamingServiceVersion,
    dockerSettings()
  )

// metrics Project
lazy val `metrics` = (project in file(s"services/$metricsService"))
  .enablePlugins(PlayScala, sbtdocker.DockerPlugin, BuildInfoPlugin, JavaAppPackaging)
  .settings(
    commonSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, BuildInfoKey.action("build_time") {
      java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.now())
    }),
    buildInfoPackage := "controllers",
    buildInfoObject := "BuildInfo",
    buildInfoOptions += BuildInfoOption.ToJson,
    libraryDependencies ++= Seq(filters) ++ akkaTyped ++ playLibs ++ testLibs ++ logstash,
    playSettings,
    scalacOptions ++= scalaCompilerOptions.value,
    version := metricsServiceVersion,
    dockerSettings()
  )

// Global commands
Global / concurrentRestrictions := Seq(Tags.limitAll(200))
lazy val runAll = inputKey[Unit]("Runs all sub projects")
runAll := {
  (run in Compile in `streaming`).partialInput(" -Dplay.server.http.port=9000").evaluated
  (run in Compile in `metrics`).partialInput(" -Dplay.server.http.port=9090").evaluated
}

//
val host        = "registry.dev.redbee.io"
val builderHost = s"$host/decidir2"

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
      from(s"$builderHost/openjdk:8u212-jre")
      maintainer("redbee studios")
      expose(9000, 9443)
      workDir("/opt/docker")
      add(appDir, "/opt/docker")
      add(finder.get, "/usr/lib/jvm/default-jvm/jre/lib/security/")
      entryPoint("/opt/docker/bin/wrapper.sh")
    }
  }
)

// Run checkstyle before compile
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := scalastyle.in(Compile).toTask("").value
compileScalastyle := (compileScalastyle triggeredBy (scalafmt in Compile)).value
