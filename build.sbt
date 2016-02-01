name := "quill-pgsql"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "io.getquill" %% "quill-jdbc" % "0.3.0",
  "org.specs2" %% "specs2-core" % "3.7" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")