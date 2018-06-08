package Quantum

import Quantum.Graphics.{Frame, Window}
import Quantum.Math.{Field, Quaternion, Sphere, Vector3}
import Quantum.Universe.{Figures, Space}
import Quantum.Enrichment.EnrichNumerical
import Quantum.Utils.Color.EnrichDoubleTuple
import cats.implicits._

object Main {

  val depth = 7

  /* Width and height of the frame, in virtual pixels */
  val frameWidth: Int = 192
  val frameHeight: Int = 108

  val multiplier: Int = 3

  /* Width and height of the window, in physical pixels */
  val windowWidth: Int = frameWidth * multiplier
  val windowHeight: Int = frameHeight * multiplier

  /* Define the field of view horizontally and vertically */
  val horizontalRot = new Quaternion(0d, 1d, 0d, 0.5d)
  val verticalRot = new Quaternion(1d, 0d, 0d, 0.5d * (frameHeight / frameWidth.d))

  /* Define the view direction for each pixel using the two rotations defined earlier */
  val view = new Field(
    frameWidth,
    frameHeight,
    { (x: Int, y: Int) =>
      Vector3(0d, 0d, 1d) *
        (horizontalRot * ((x / frameWidth.d) * 2d - 1d)) *
        (verticalRot * ((y / frameHeight.d) * 2d - 1d))
    }
  )


  /* Define the user perspective */
  var position = Vector3(0, 0, -4)
  var rotation = new Quaternion(0, 1, 0, 0)
  val rotVel = new Quaternion(0, 1, 0, 0.0005)

  def main(args: Array[String]): Unit = {
    Logger.info("Running Quantum...")

    val a = args.map(
      s => {
        val split = s.split("=")

        split.length match {
          case 1 => (split.head, "")
          case 2 => (split(0), split(1))
          case _ => (s, "")
        }
      }
    )

    /* Parse arguments */
    a.foreach {
      case ("-debug", _) => Logger.enableDebug(true)
      case ("-construct", _) => ()
      case ("-texture", _) => ()
      case s => Logger.info(s"Unknown parameter $s.")
    }

    val construct: Sphere => Boolean = Figures.constructs.getOrElse(
      a.find(p => p._1 == "-construct").separate._2.getOrElse("torus2"),
      {
        Logger.error("Could not find construct!")
        _ => false
      }
    )

    val texture: Vector3 => Int = Figures.textures.getOrElse(
      a.find(p => p._1 == "-texture").separate._2.getOrElse("rainbow"),
      {
        Logger.error("Could not find texture!")
        _ => 0
      }
    )

    def figure(s: Sphere) = if(construct(s)) Some(texture(s.position)) else None

    /* Create the universe from our construct, configure the recursion depth of our Space here */
    val count: Long = math.round((1.0 / 7.0) * (math.pow(8, depth) - 1))
    Logger.info(s"Creating a universe with $count spaces.")
    val universe: Space[Int] = Universe.fill[Int](depth, Sphere(Vector3(0d, 0d, 0d), 1), figure)

    /* Define the shader used to determine the color of each virtual pixel */
    def shader(x: Int, y: Int, t: Long): Int = {
      universe.find(position * (rotVel * t.d), view(x, y) * rotation * (rotVel * t.d)) match {
        case some: Some[Int] => some.value
        case None => 0x2c2c2c
      }
    }

    val frame = new Frame(frameWidth, frameHeight)
    val window = new Window("Quantum", windowWidth, windowHeight, frame, shader)
    window.loop()
    window.destroy()
  }
}
