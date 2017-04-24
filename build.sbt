// *****************************************************************************
// Projects
// *****************************************************************************

lazy val amwsScala =
  project
    .in(file("."))
    .enablePlugins(GitBranchPrompt, GitVersioning)
    .settings(settings)
    .settings(
      name := "amws-scala",
      libraryDependencies ++= Seq(
        library.akkaHttp,
        library.scalaCheck      % Test,
        library.scalaTest       % Test
      ),
      wartremoverWarnings in (Compile, compile) ++= Warts.unsafe
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val akkaHttp        = "10.0.5"
      val cats            = "0.9.0"
      val scalaCheck      = "1.13.5"
      val scalaTest       = "3.0.3"
    }
    val akkaHttp        = "com.typesafe.akka" %% "akka-http"         % Version.akkaHttp
    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp
    val cats            = "org.typelevel"     %% "cats"              % Version.cats
    val scalaCheck      = "org.scalacheck"    %% "scalacheck"        % Version.scalaCheck
    val scalaTest       = "org.scalatest"     %% "scalatest"         % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  gitSettings

lazy val commonSettings =
  Seq(
    scalaVersion in ThisBuild := "2.12.2",
    crossScalaVersions := Seq("2.11.11", "2.12.2"),
    organization := "com.wegtam.tensei",
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-target:jvm-1.8",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xfuture",
      "-Xlint",
      "-Ydelambdafy:method",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    incOptions := incOptions.value.withNameHashing(nameHashing = true),
    autoAPIMappings := true
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

