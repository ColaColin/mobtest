import play.Project._ 

import com.github.play2war.plugin._ 

name := "mobtest"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

Play2WarPlugin.play2WarSettings 

Play2WarKeys.servletVersion := "3.0"