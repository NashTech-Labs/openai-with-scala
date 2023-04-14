import sbtsonar.SonarPlugin.autoImport.sonarProperties

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "OpenAIPracticeV1"
  )

libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.10",
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.10",
  "com.typesafe.akka" %% "akka-actor" % "2.6.1",
  "io.cequence" %% "openai-scala-client" % "0.3.1",

  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.mockito" % "mockito-core" % "5.2.0" % Test
)

sonarProperties := Map(
  "sonar.coverage.exclusions" -> "**Driver.scala"
)
