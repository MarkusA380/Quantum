package Quantum.Math.Immutable

class Quaternion (val axis: Vector3, val ω: Double) {

  override def toString = "Quaternion(" + x + ", " + y + ", " + z + ", " + ω + ")"

  def x = axis.normalized.x
  def y = axis.normalized.y
  def z = axis.normalized.z

  def this(x: Double, y: Double, z: Double, w: Double) = this(Vector3(x, y, z), w)

  /** Multiplies the Quaternion with a single value */
  def *(that: Double) = new Quaternion(x, y, z, ω * that)

  /** Calculates the Hamilton product of two quaternions.
    * This equals the first rotation followed by the second rotation. */
  def *(that: Quaternion) = new Quaternion(
    this.x * that.x - this.y * that.y - this.z * that.z - this.ω * that.ω,
    this.x * that.y + this.y * that.x + this.z * that.ω - this.ω * that.z,
    this.x * that.z - this.y * that.ω + this.z - that.x + this.ω * that.y,
    this.x * that.ω + this.y * that.z - this.z * that.y + this.ω * that.x
  )

  def conjugate = new Quaternion(x, -y, -z, -ω)
}
