// *****************************************************************************
// Projects
// *****************************************************************************

lazy val amwsMacros =
  project
  .in(file("macros"))
  .settings(settings)
  .settings(
    name := "amws-macros",
    libraryDependencies ++= Seq(
      "org.scala-lang"  % "scala-reflect" % scalaVersion.value,
      library.scalaTest % Test
    ),
    scalacOptions ++= Seq(
      "-language:experimental.macros"
    ),
    publish := {}, // Disable publishing of macro artefact.
    publishLocal := {} // Disable publishing of macro artefact.
  )
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

lazy val amwsScala =
  project
    .in(file("."))
    .dependsOn(amwsMacros % "compile-internal, test-internal") // Prevent dependency on published macro artefact.
    .enablePlugins(AutomateHeaderPlugin, GitBranchPrompt, GitVersioning)
    .settings(settings)
    .settings(
      name := "amws-scala",
      libraryDependencies ++= Seq(
        library.akkaHttp,
        library.akkaStream,
        library.catsCore,
        library.yaidom,
        library.akkaHttpTestkit % Test,
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
      val akkaHttp        = "10.1.1"
      val akkaStream      = "2.5.12"
      val cats            = "1.1.0"
      val scalaCheck      = "1.14.0"
      val scalaTest       = "3.0.7"
      val shapeless       = "2.3.2"
      val yaidom          = "1.8.0"
    }
    val akkaHttp        = "com.typesafe.akka"   %% "akka-http"         % Version.akkaHttp
    val akkaHttpTestkit = "com.typesafe.akka"   %% "akka-http-testkit" % Version.akkaHttp
    val akkaStream      = "com.typesafe.akka"   %% "akka-stream"       % Version.akkaStream
    val catsCore        = "org.typelevel"       %% "cats-core"         % Version.cats
    val scalaCheck      = "org.scalacheck"      %% "scalacheck"        % Version.scalaCheck
    val scalaTest       = "org.scalatest"       %% "scalatest"         % Version.scalaTest
    val shapeless       = "com.chuusai"         %% "shapeless"         % Version.shapeless
    val yaidom          = "eu.cdevreeze.yaidom" %% "yaidom"            % Version.yaidom
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  gitSettings ++
  headerSettings ++
  publishSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    scalaVersion in ThisBuild := "2.12.8",
    crossScalaVersions := Seq(scalaVersion.value),
    organization := "com.wegtam",
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    addCompilerPlugin("org.spire-math" % "kind-projector" % "0.9.10" cross CrossVersion.binary),
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
      "-Ypartial-unification",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),
    scalacOptions ++= {
      if (scalaVersion.value.startsWith("2.11"))
        Seq("-Xmax-classfile-name", "78")
      else
	Seq()
    },
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    autoAPIMappings := true
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

// License is set in publishSettings.
lazy val headerSettings =
  Seq(
    organizationName := "Contributors as noted in the AUTHORS.md file",
    startYear := Some(2017),
  )

lazy val publishSettings =
  Seq(
    bintrayOrganization := Option("wegtam"),
    bintrayPackage := "amws-scala",
    bintrayReleaseOnPublish in ThisBuild := false,
    bintrayRepository := "free",
    developers += Developer(
      "wegtam",
      "Wegtam GmbH",
      "tech@wegtam.com",
      url("https://www.wegtam.com")
    ),
    licenses += ("MPL-2.0", url("https://www.mozilla.org/en-US/MPL/2.0/")),
    pomIncludeRepository := (_ => false),
    publish := (publish dependsOn (test in Test)).value,
    publishArtifact in Test := false,
    scmInfo := Option(
      ScmInfo(
        url("https://github.com/wegtam/amws-scala"),
        "git@github.com:wegtam/amws-scala.git"
      )
    )
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )
