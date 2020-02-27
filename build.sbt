name := "scalaflix"
organization in ThisBuild := "redbee"
scalaVersion in ThisBuild := "2.12.3"
val versionStr = "0.0.1-SNAPSHOT"
version := versionStr

// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    common,
    core,
    metrics
  )

lazy val common = project
  .settings(
    name := "common",
    settings
  )

lazy val core = project
  .enablePlugins(PlayScala)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "core",
    version := versionStr,
    msSettings,
    libraryDependencies ++= commonDependencies ++ coreDependencies,
    buildInfoOptions += BuildInfoOption.ToJson
  )
  .dependsOn(
    common
  )

lazy val metrics = project
  .enablePlugins(PlayScala)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "metrics",
    version := versionStr,
    msSettings,
    libraryDependencies ++= commonDependencies ++ metricsDependencies,
    buildInfoOptions += BuildInfoOption.ToJson
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val circeV            = "0.12.3"
    val circeCore         = "io.circe" %% "circe-core" % circeV
    val circeGeneric      = "io.circe" %% "circe-generic" % circeV
    val circeParser       = "io.circe" %% "circe-parser" % circeV
    val circeExtras       = "io.circe" %% "circe-generic-extras" % "0.12.2"
    val playCirce         = "com.dripower" %% "play-circe" % "2712.0"
    val minio             = "io.minio" % "minio" % "6.0.13"
    val logstash          = "net.logstash.logback" % "logstash-logback-encoder" % "5.1"
    val xuggle            = "xuggle" % "xuggle-xuggler" % "5.4"
    val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
  }

lazy val commonDependencies = Seq(
  guice,
  ws,
  dependencies.scalaTestPlusPlay,
  dependencies.circeCore,
  dependencies.circeGeneric,
  dependencies.circeParser,
  dependencies.circeExtras,
  dependencies.playCirce,
  dependencies.logstash
)

lazy val coreDependencies = Seq(
  dependencies.minio,
  dependencies.xuggle
)

lazy val metricsDependencies = Seq()

// SETTINGS

lazy val settings = commonSettings ++ scalafmtSettings

lazy val msSettings = settings ++ buildInfoSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    "Dcm4Che Repository" at "https://www.dcm4che.org/maven2",
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "config",
  buildInfoOptions += BuildInfoOption.ToJson
)

lazy val scalafmtSettings = Seq(
  scalafmtOnCompile := true,
  scalafmtTestOnCompile := true,
  scalafmtVersion := "1.2.0"
)

val runAll = inputKey[Unit]("Runs all sub projects")
runAll := {
  (run in Compile in core).partialInput(" -Dplay.server.http.port=9000").evaluated
  (run in Compile in metrics).partialInput(" -Dplay.server.http.port=9001").evaluated
}

Global / concurrentRestrictions := Seq(Tags.limitAll(5))
