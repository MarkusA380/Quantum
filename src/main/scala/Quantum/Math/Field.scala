package Quantum.Math

import scala.collection.mutable.ArrayBuffer

class Field(data: Vector[ArrayBuffer[Vector3]]) {

  /** Create a new Field using a function that defines the initial value of each element.
    * @param width The width of the frame
    * @param height The height of the frame
    * @param f A function that maps the coordinates of an element to its value
    */
  def this(width: Int, height: Int, f: (Int, Int) => Vector3) = this(
    Vector.range(0, width).map(
      x => ArrayBuffer.range(0, height).map(
        y => f(x, y)
      )
    )
  )

  def apply(x: Int, y: Int): Vector3 = data(x)(y)
}
