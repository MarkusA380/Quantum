package Quantum.Math.Immutable

import Quantum.Math.MatrixOperationException

import scala.collection.immutable.Vector
import scala.math.sqrt

/* */

class Matrix[A] (val data: Vector[Vector[A]]) {

  println("A new Matrix has been created.")

  /** Create a new Matrix using a function that defines the initial value of each element. */
  def this(width: Int, height: Int, f: (Int, Int) => A) = this(
    Vector.range(0, width).map(
      x => Vector.range(0, height).map(
        y => f(x, y)
      )
    )
  )

  /** Returns the n-th row of the matrix. */
  def col(n: Int) = data(n)

  /** Returns the n-th column of the matrix. */
  def row(n: Int) = data.map(_ (n))

  /** Returns a specific element (x,y) in the matrix. */
  def apply(x: Int, y: Int) = data(x)(y)

  /** Returns the number of elements each row contains. */
  def width = data.length

  /** Returns the number of elements each column contains. */
  def height = data.map(_.length).min

  /** Transposes the matrix. */
  def transpose = new Matrix(height, width, (x: Int, y: Int) => data(y)(x))

  /** Implementation of map for the matrix
    * @param f The function that maps each element to a new value
    **/
  def map[B](f: A => B) = new Matrix(data.map(_.map(f)))
}

object Matrix {
  implicit class MatrixOps(left: Matrix[Double]) {

    /** Calculates the norm of the matrix. */
    def normalize = sqrt(left.data.map(v => v.map(a => a * a).sum).sum)

    /** Multiplies the matrix with another matrix.
      * Note: The first matrix must be exactly as wide as the second matrix is high. */
    def *(right: Matrix[Double]) =
      if (left.width == right.height) new Matrix(
        left.height,
        right.width,
        (x, y) => (left.row(y), right.col(x)).zipped.map((x, y) => x + y).sum
      )
      else throw new Exception(
        "Couldn't multiply matrices with dimensions "
        + "(" + left.width + ", " + left.height + ") and (" + right.width + ", " + right.height + ")"
      )

    /** Multiplies the matrix with a single value. */
    def *(right: Double) = new Matrix(left.width, left.height, (x, y) => left.data(x)(y) * right)

    /** Sums two matrices.
      * Note: The two matrices must have the same dimensions. */
    def +(right: Matrix[Double]) =
      if (left.width == right.width && left.height == right.height) new Matrix(
        (left.data, right.data).zipped.map(
          (v1, v2) => (v1, v2).zipped.map(
            (a, b) => a + b
          )
        )
      )
      else throw MatrixOperationException(
        "Couldn't sum matrices with dimensions "
        + "(" + left.width + ", " + left.height + ") and (" + right.width + ", " + right.height + ")"
      )
  }
}
