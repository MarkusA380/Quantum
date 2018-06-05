package Quantum

import Quantum.Graphics.{Frame, Window}
import Quantum.Math.{Quaternion, Vector3}
import Quantum.Universe.Space
import Quantum.Enrichment._
import Quantum.Utils.Color._

object Main {

  /** The blueprint for our universe that decides which values the Volumes will be filled with
    *
    * @param pos: The position of the Volume in question
    * @param radius: The radius of the Volume in question
    **/
  def construct(pos: Vector3, radius: Double) = {
    def f(d: Double) = (d + 1) / 2
    Some(
      (f(pos.x), f(pos.y), f(pos.z)).toColor
    )
  }

  /* Create the universe from our construct, configure the recursion depth of our Space here */
  val universe: Space[Int] = Space.fill[Int](4, Vector3(0d, 0d, 0d), 1, construct)

  /* Width and height of the frame, in virtual pixels */
  val frameWidth = 192
  val frameHeight = 108

  /* Width and height of the window, in physical pixels */
  val windowWidth = 960
  val windowHeight = 540

  /* Define the field of view horizontally and vertically */
  val horizontalRot = new Quaternion(0d, 1d, 0d, 0.5d)
  val verticalRot = new Quaternion(1d, 0d, 0d, 0.5d * (frameHeight / frameWidth.d))

  /* Define the view direction for each pixel using the two rotations defined earlier */
  def view(x: Int, y: Int): Vector3 = {
    Vector3(0d, 0d, 1d) *
      (horizontalRot * ((x / frameWidth.d) * 2d - 1d)) *
      (verticalRot * ((y / frameHeight.d) * 2d - 1d))
  }

  /* Define the user perspective */
  var position = Vector3(0, 0, -4)
  var rotation = new Quaternion(0, 1, 0, 0)
  val rotVel = new Quaternion(0, 1, 0, 0.001)

  /* The shader used to determine each virtual pixels color */
  def shader(x: Int, y: Int, t: Long): Int = {
    universe.find(position * (rotVel * t.d), view(x, y) * rotation * (rotVel * t.d)) match {
      case some: Some[Int] => some.value
      case None => 0x2c2c2c
    }
  }

  def main(args: Array[String]): Unit = {
    Logger.info(s"Running Quantum...")

    /* Parse arguments */
    args.foreach {
      case "-debug" => Logger.enableDebug(true)
      case s: String => Logger.info(s"Unknown parameter $s.")
    }

    val frame = new Frame(frameWidth, frameHeight, (_, _) => 0xff0000)
    val window = new Window("Quantum", windowWidth, windowHeight, frame, shader)
    window.loop()
    window.destroy()
  }
}
