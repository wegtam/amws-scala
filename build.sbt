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
        library.http4sDsl,
        library.http4sBlazeClient,
        library.yaidom,
        library.akkaHttpTestkit   % Test,
        library.http4sBlazeServer % Test,
        library.scalaCheck        % Test,
        library.scalaCheckTools   % Test,
        library.scalaTest         % Test,
        library.scalaTestPlus     % Test
      ),
      wartremoverWarnings in (Compile, compile) ++= Warts.unsafe
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val akkaHttp        = "10.1.12"
      val akkaStream      = "2.6.6"
      val cats            = "2.1.1"
      val http4s          = "0.21.4"
      val scalaCheck      = "1.14.3"
      val scalaCheckTools = "0.3.5"
      val scalaTest       = "3.2.0"
      val scalaTestPlus   = "3.1.2.0"
      val shapeless       = "2.3.2"
      val yaidom          = "1.11.0"
    }
    val akkaHttp          = "com.typesafe.akka"   %% "akka-http"                   % Version.akkaHttp
    val akkaHttpTestkit   = "com.typesafe.akka"   %% "akka-http-testkit"           % Version.akkaHttp
    val akkaStream        = "com.typesafe.akka"   %% "akka-stream"                 % Version.akkaStream
    val catsCore          = "org.typelevel"       %% "cats-core"                   % Version.cats
    val http4sBlazeServer = "org.http4s"          %% "http4s-blaze-server"         % Version.http4s
    val http4sBlazeClient = "org.http4s"          %% "http4s-blaze-client"         % Version.http4s
    val http4sDsl         = "org.http4s"          %% "http4s-dsl"                  % Version.http4s
    val scalaCheck        = "org.scalacheck"      %% "scalacheck"                  % Version.scalaCheck
    val scalaCheckTools   = "com.47deg"           %% "scalacheck-toolbox-datetime" % Version.scalaCheckTools
    val scalaTest         = "org.scalatest"       %% "scalatest"                   % Version.scalaTest
    val scalaTestPlus     = "org.scalatestplus"   %% "scalacheck-1-14"             % Version.scalaTestPlus
    val shapeless         = "com.chuusai"         %% "shapeless"                   % Version.shapeless
    val yaidom            = "eu.cdevreeze.yaidom" %% "yaidom"                      % Version.yaidom
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

def compilerSettings(sv: String) =
  CrossVersion.partialVersion(sv) match {
    case Some((2, 13)) =>
      Seq(
        "-deprecation",
        "-explaintypes",
        "-feature",
        "-language:_",
        "-unchecked",
        "-Xcheckinit",
        "-Xfatal-warnings",
        "-Xlint:adapted-args",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Ywarn-dead-code",
        "-Ywarn-extra-implicit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused:implicits",
        "-Ywarn-unused:imports",
        "-Ywarn-unused:locals",
        "-Ywarn-unused:params",
        "-Ywarn-unused:patvars",
        "-Ywarn-unused:privates",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:last-modified",
        "-Ycache-macro-class-loader:last-modified",
        "-Ymacro-annotations" // Needed for Simulacrum
      )
    case _ =>
      Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-explaintypes",
      "-feature",
      "-language:_",
      "-target:jvm-1.8",
      "-unchecked",
      "-Xcheckinit",
      "-Xfatal-warnings",
      "-Xfuture",
      "-Xlint:adapted-args",
      "-Xlint:by-name-right-associative",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-override",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unsound-match",
      "-Ydelambdafy:method",
      "-Yno-adapted-args",
      "-Ypartial-unification",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    )
  }

lazy val commonSettings =
  Seq(
    scalaVersion in ThisBuild := "2.13.2",
    crossScalaVersions := Seq(scalaVersion.value, "2.12.10"),
    organization := "com.wegtam",
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full),
    scalacOptions ++= compilerSettings(scalaVersion.value),
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
    autoAPIMappings := true,
    Compile / console / scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused-import"),
    Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
    Test / console / scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused-import"),
    Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value)
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
