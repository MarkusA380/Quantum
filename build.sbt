name := "Quantum"
version := "0.1"
scalaVersion := "2.12.6"

val lwjgl = {
  val version = "3.1.6"
  val native = "windows"
  val modules = Seq(
    "lwjgl",
    "lwjgl-glfw",
    "lwjgl-opengl"
  )

  modules.flatMap{
    s => {
      val classifier = s"$s-natives-$native"

      Seq(
        "lwjgl" % s % version from s"https://build.lwjgl.org/release/$version/bin/$s/$s.jar",
        "lwjgl" % classifier % version from s"https://build.lwjgl.org/release/$version/bin/$s/$classifier.jar"
      )
    }
  }
}

libraryDependencies ++= {
  lwjgl ++
  Seq(
    "io.monix" % "monix_2.12" % "3.0.0-8084549"
  )
}
