name := "scala_smile_ML_playground"

version := "1.0"

scalaVersion := "2.11.7"

scalaVersion in ThisBuild := "2.11.7"

resolvers += "Akka Repo" at "http://repo.akka.io/releases" 
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype Repo" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Twitter Repo" at "http://maven.twttr.com/"

libraryDependencies += "com.github.haifengl" % "smile-core" % "1.0.3"
libraryDependencies += "com.github.haifengl" % "smile-plot" % "1.0.3"
libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.11" % "2.0.0-M2"

