
name := "datadog-reporter"

version := "1.0"

scalaVersion := "2.11.11"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  "org.coursera" % "metrics-datadog" % "1.1.6"
)
  .map(_.exclude("ch.qos.logback","logback-classic"))
  .map(_.exclude("ch.qos.logback","logback-classic"))

libraryDependencies += "com.quantifind" % "KafkaOffsetMonitor" % "0.4.1-SNAPSHOT" % "provided" from "https://github.com/Morningstar/kafka-offset-monitor/releases/download/0.4.1/KafkaOffsetMonitor-assembly-0.4.1-SNAPSHOT.jar"


val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case meta(_) => MergeStrategy.discard // invalid signature file digest for manifest main attributes from spark.sql
  case _ => MergeStrategy.first
}
