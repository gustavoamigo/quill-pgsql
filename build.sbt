name := "quill-pgsql"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4.1207",
  "io.getquill" %% "quill-jdbc" % "0.3.1",
  "org.specs2" %% "specs2-core" % "3.7" % "test",
  "com.typesafe.play" %% "play-json" % "2.4.6"
)

scalacOptions in Test ++= Seq("-Yrangepos")