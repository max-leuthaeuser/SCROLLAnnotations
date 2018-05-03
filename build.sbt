name := "SCROLLAnnotations"
scalaVersion := "2.12.6"
version := "0.0.1"
organization := "com.github.max-leuthaeuser"

javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:reflectiveCalls",
  "-target:jvm-1.8",
  "-encoding", "utf8",
  "-Xlint",
  "-Xlint:-missing-interpolator",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import")

libraryDependencies ++= Seq(
  "com.github.max-leuthaeuser" %% "scroll" % "1.5",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

test in assembly := {}

assembleArtifact in assemblyPackageScala := false

assemblyMergeStrategy in assembly := {
  case PathList(ps@_*)
    if ps.last.endsWith("plugin.xml") ||
      ps.last.endsWith("properties") ||
      ps.last.endsWith(".exsd") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

scalacOptions in console in Compile += ((assembly in Compile) map {
  pluginJar => "-Xplugin:" + pluginJar
}).value

scalacOptions in Test ++= ((assembly in Compile) map {
  pluginJar => Seq("-Xplugin:" + pluginJar, "-Jdummy=" + pluginJar.lastModified)
}).value

artifact in(Compile, assembly) := {
  val art = (artifact in(Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in(Compile, assembly), assembly)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra :=
  <url>https://github.com/max-leuthaeuser/SCROLLAnnotations</url>
    <licenses>
      <license>
        <name>LGPL 3.0 license</name>
        <url>http://www.opensource.org/licenses/lgpl-3.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/max-leuthaeuser/SCROLLAnnotations.git</connection>
      <developerConnection>scm:git:git@github.com:max-leuthaeuser/SCROLLAnnotations.git</developerConnection>
      <url>github.com/max-leuthaeuser/SCROLLAnnotations</url>
    </scm>
    <developers>
      <developer>
        <id>max-leuthaeuser</id>
        <name>Max Leuthaeuser</name>
        <url>https://wwwdb.inf.tu-dresden.de/rosi/investigators/doctoral-students/</url>
      </developer>
    </developers>
