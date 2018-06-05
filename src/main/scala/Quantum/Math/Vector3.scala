package Quantum.Math

import scala.math.{cos, sin, sqrt}

case class Vector3(x: Double, y: Double, z: Double) {

  override def toString = "Vector3(" + x + ", " + y + ", " + z + ")"

  /** Calculates the sum of two vectors */
  def +(that: Vector3) = Vector3(
    this.x + that.x,
    this.y + that.y,
    this.z + that.z
  )

  /** Calculates the inverse sum of two vectors */
  def -(that: Vector3) = Vector3(
    this.x - that.x,
    this.y - that.y,
    this.z - that.z
  )

  /** Calculates the dot product of two vectors */
  def ○(that: Vector3) = this.x * that.x + this.y * that.y + this.z * that.z

  /** Calculates the cross product of two vectors */
  def ✕(that: Vector3) = Vector3(
    this.y * that.z - this.z * that.y,
    this.z * that.x - this.x * that.z,
    this.x * that.y - this.y * that.x
  )

  /** Multiplies the vector wit a scalar */
  def *(that: Double) = Vector3(
    this.x * that,
    this.y * that,
    this.z * that
  )

  /** Rotate the vector by a quaternion */
  def *(that: Quaternion): Vector3 = {
    /* Rotate the vector using Rodrigues` rotation formula */
    this * cos(that.ω) + (that.axis ✕ this) * sin(that.ω) + that.axis * (that.axis ○ this) * (1d - cos(that.ω))
  }

  /** Calculates the length of the vector */
  def length = sqrt(x * x + y * y + z * z)

  /** Normalizes the vector */
  def normalized = this * (1d / this.length)
}