name := "Quantum"
version := "0.1"
scalaVersion := "2.12.6"


val os = "windows"
val lwjglVersion = "3.1.6"
val lwjglModules = Seq("lwjgl", "lwjgl-glfw", "lwjgl-opengl").map(s => (s, s))
val lwjglJars = lwjglModules ++ lwjglModules.map(t => (t._1, t._2 + s"-natives-$os"))
val lwjglDepend = lwjglJars.map(t => "lwjgl" % t._2 % "3.1.6" from s"https://build.lwjgl.org/release/$lwjglVersion/bin/${t._1}/${t._2}.jar")

libraryDependencies ++= {
  lwjglDepend
}

libraryDependencies ++= {
  Seq(
    "io.monix" % "monix_2.12" % "3.0.0-8084549"
  )
}