name := "quill-hikari-issue"
version := "1.0"
scalaVersion := "2.13.7"

lazy val zio = "dev.zio" %% "zio" % "1.0.12"
lazy val postgresql = "org.postgresql" % "postgresql" % "42.3.1"
lazy val `quill-jdbc-zio` = "io.getquill" %% "quill-jdbc-zio" % "3.10.0"

libraryDependencies ++= Seq(
  zio,
  postgresql,
  `quill-jdbc-zio`
)
