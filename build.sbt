// *****************************************************************************
// Projects
// *****************************************************************************

lazy val amwsScala =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitBranchPrompt, GitVersioning)
    .settings(settings)
    .settings(
      name := "amws-scala",
      libraryDependencies ++= Seq(
        library.akkaHttp,
	library.akkaStream,
        library.catsCore,
        library.yaidom,
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
      val akkaHttp        = "10.1.0"
      val akkaStream      = "2.5.11"
      val cats            = "1.1.0"
      val scalaCheck      = "1.13.5"
      val scalaTest       = "3.0.5"
      val shapeless       = "2.3.2"
      val yaidom          = "1.8.0-M2"
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
  publishSettings

lazy val commonSettings =
  Seq(
    scalaVersion in ThisBuild := "2.12.5",
    crossScalaVersions := Seq("2.11.12", scalaVersion.value),
    organization := "com.wegtam",
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
      "-Ypartial-unification",
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
