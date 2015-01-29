name := """PistachoTV2.1"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "0.8.1",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "org.apache.lucene" % "lucene-core" % "4.10.3",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.10.3",
  "org.apache.lucene" % "lucene-queryparser" % "4.10.3",
  jdbc,
  anorm,
  cache,
  ws
)
