name := "Quantum"
version := "0.1"
scalaVersion := "2.12.6"

libraryDependencies ++= {
  val version = "3.1.6"

  Seq(
    "lwjgl",
    "lwjgl-glfw",
    "lwjgl-opengl"
  ).flatMap {
    module => {
      Seq(
        "org.lwjgl" % module % version,
        "org.lwjgl" % module % version classifier "natives-windows"
      )
    }
  }
}

libraryDependencies += "io.monix" % "monix_2.12" % "3.0.0-8084549"
