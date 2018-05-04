package Quantum

import java.awt.{Color, Dimension}

import Quantum.Graphics.Canvas
import Quantum.Universe.Space
import Quantum.Utils._
import Quantum.Enrichment._
import Quantum.Math.Immutable.{Quaternion, Vector3}
import Quantum.Math.Mutable.Matrix
import monix.reactive.Observable

import scala.concurrent.duration._
import scala.swing.{MainFrame, SimpleSwingApplication}
import monix.execution.Scheduler.Implicits.global

object Main extends SimpleSwingApplication {

  /** The blueprint for our universe that decides which values the Volumes will be filled with
    *
    * @param pos: The position of the Volume in question
    * @param radius: The radius of the Volume in question
    **/
  def construct(pos: Vector3, radius: Double) = {
    if (true) Some(new Color(random.f, random.f, random.f))
    else None
  }

  /* Create the universe from our construct, configure the recursion depth of our Space here */
  val universe = Space.fill[Color](5, Vector3(0d, 0d, 0d), 1, construct)

  /* Width and height of our window in pixels.
     The window can still be resized but the amount of virtual pixels will stay the same. */
  val width = 200
  val height = 200

  /* Define the field of view horizontally and vertically */
  val horizontalRot = new Quaternion(0d, 1d, 0d, 0.5d)
  val verticalRot = new Quaternion(1d, 0d, 0d, 0.5d * (height.d / width.d))

  /* Define the view direction for each pixel using the two rotations defined earlier */
  val view = new Matrix[(Int, Int, Vector3)](width, height, {
    (x, y) =>
      (
        x,
        y,
        Vector3(0d, 0d, 1d) * (horizontalRot * ((x.d / width.d) * 2d - 1d)) * (verticalRot * ((y.d / height.d) * 2d - 1d))
      )
  })

  /* Define the user perspective */
  var position = Vector3(0, 0, -4)
  var rotation = new Quaternion(0, 1, 0, 0)

  val rotVel = new Quaternion(0, 1, 0, 0.0001)

  def shader(x: Int, y: Int) = {
    universe.find(position * (rotVel * time.d), view(x, y)._3 * rotation * (rotVel * time.d)) match {
      case some: Some[Color] => some.value
      case None => Color.black
    }
  }

  var canvas = new Canvas(new Matrix[Color](width, height, shader)) {
    preferredSize = new Dimension(width, height)
  }

  /** This function is originally declared by Swing. It defines the window content. */
  def top = new MainFrame {
    contents = canvas

    val renderStart = time
    /* Create an Observable that runs the same code over and over again */
    Observable.interval(Duration.Zero).map{i =>
      contents.head match {
        case canvas: Canvas =>
          /* Reevaluate shader for each screen space coordinate */
          canvas.matrix.foreachParallel((x, y) => shader(x, y), 10)

          println("Frame delta: " + ((time - renderStart) / (i + 1)).toString + " ms")
      }
      /* Repaint the window */
      repaint
    }.subscribe /* You have to subscribe to the Observable or it will not be evaluated. */
  }
}