package Quantum.Math.Immutable

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

  /** Multiplies the axis with a value */
  def *(that: Double) = Vector2(
    this.x * that,
    this.y * that
  )

  /** Calculates the length the axis */
  def length = sqrt(x * x + y * y)

  /** Normalizes the axis */
  def normalized = this * (1d / this.length)
}