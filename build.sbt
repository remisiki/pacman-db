import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

lazy val root = (project in file("."))
	.settings(
		name := "mysql",
		libraryDependencies += scalaTest % Test
	)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
libraryDependencies ++= Seq(
	"mysql" % "mysql-connector-java" % "8.0.30",
	"org.scalikejdbc" %% "scalikejdbc"       % "4.0.0",
	"org.scalikejdbc" %% "scalikejdbc-config"  % "4.0.0",
	"ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)