addSbtPlugin("org.foundweekends" % "sbt-bintray"     % "0.5.1")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"         % "0.9.3")
addSbtPlugin("de.heikoseeberger" % "sbt-header"      % "5.0.0")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"         % "1.1.0")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"    % "1.4.0")
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "2.2.1")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git
