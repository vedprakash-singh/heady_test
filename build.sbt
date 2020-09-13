name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.11.12"

lazy val akkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.18.7",
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.json4s" %% "json4s-native" % "3.5.4",
  "ch.megard" %% "akka-http-cors" % "0.3.0"
)
