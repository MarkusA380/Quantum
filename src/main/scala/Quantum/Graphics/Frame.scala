package Quantum.Graphics

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Frame(data: Vector[ArrayBuffer[Int]]) {

  /** Create a new Frame filled with zeroes.
    * @param width The width of the frame
    * @param height The height of the frame
    */
  def this(width: Int, height: Int) = this(
    Vector.fill(width)(ArrayBuffer.fill(height)(0))
  )

  /** Returns the value with the given coordinates */
  def apply(x: Int, y: Int): Int = data(x)(y)

  /** Returns the number of elements each row contains. */
  def width: Int = data.length

  /** Returns the number of elements each column contains.*/
  def height: Int = data.head.length

  /** Updates every element of the frame.
    * @param f A function that maps the coordinates of an element to its value
    * @return The identity of the frame
    */
  def update(f: (Int, Int) => Int): Frame = {
    for(x <- Range(0, data.length, 1)){
      for(y <- Range(0, data.head.length, 1)){
        data(x)(y) = f(x, y)
      }
    }
    this
  }

  /** Updates every element of the Frame using parallel execution
    * @param f A function that maps the coordinates of an element to its value
    * @return The identity of the frame
    */
  def update(f: (Int, Int) => Int, parallelism: Int): Frame = {

    val widthIndices = data.indices
    val heightIndices = data.head.indices
    val groupSize = data.length / parallelism

    Await.ready(
      Task.gatherUnordered(
        widthIndices.grouped(groupSize).map {
          seq =>
            Task(
              for(x <- seq) {
                for (y <- heightIndices) {
                  data(x)(y) = f(x, y)
                }
              }
            )
        }
      ).runAsync
      , Duration.Inf)
    this
  }
}