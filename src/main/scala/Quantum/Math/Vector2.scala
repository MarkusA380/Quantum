package Quantum.Math

import scala.math.sqrt

case class Vector2(x: Double, y: Double) {

  override def toString = "Vector2(" + x + ", " + y + ")"

  /** Calculates the sum of two vectors */
  def +(that: Vector2) = Vector2(
    this.x + that.x,
    this.y + that.y
  )

  /** Calculates the dot product of two vectors */
  def ○(that: Vector2) = this.x * that.x + this.y * that.y

  /** Multiplies the vector with a scalar */
  def *(that: Double) = Vector2(
    this.x * that,
    this.y * that
  )

  /** Calculates the length of the vector */
  def length = sqrt(x * x + y * y)

  /** Normalizes the vector */
  def normalized = this * (1d / this.length)
}