name := "korpus"

version := "1.0"

scalaVersion := "2.11.5"

crossPaths := false

libraryDependencies += "org.apache.poi" % "poi" % "3.13"
libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.13"
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.13"
libraryDependencies += "joda-time" % "joda-time" % "2.8.2"
libraryDependencies += "org.joda" % "joda-convert" % "1.8"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.2"


libraryDependencies += "junit" % "junit" % "4.12" % "test"
libraryDependencies += "org.specs2" %% "specs2-core" % "3.6.5" % "test"
libraryDependencies += "org.specs2" %% "specs2-junit" % "3.6.5" % "test"
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11.0-M7"

scalacOptions in Test ++= Seq("-Yrangepos")

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
EclipseKeys.withSource := true