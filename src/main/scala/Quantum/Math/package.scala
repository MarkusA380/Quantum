package Quantum

package object Math {

  trait Spherical {
    val position: Vector3
    val radius: Double
  }

  case class Sphere(position: Vector3, radius: Double)
}
