package Quantum.Universe

import Quantum.Math.Immutable.Vector3

trait Space[A] {

  println("A new Space has been created.")

  val position: Vector3
  val radius: Double
  /** Given an origin and a direction, this function finds the value of the nearest Volume that intersects the line.
    *
    * @param origin: The origin of the line
    * @param dir: The direction of the line
    **/
  def find(origin: Vector3, dir: Vector3): Option[A] = this match {

    case volume: Volume[A] => volume.value

    case void: Void[A] =>
      val hit = void.children.filter(
        p = child => {
          val v = 2d * (dir ○ (origin - child.position))

          if(
            v * v - 4d * (dir ○ dir) * (
              ((origin - child.position) ○ (origin - child.position))
              - child.radius * child.radius
            ) >= 0d
          ) true
          else false
        }
      )

      /* SortBy seems to be really compute intensive. Is that possible? */
      val ordered = hit.sortBy(a => (a.position - origin).length)
      var result: Option[A] = None
      var i = 0
      while(result.isEmpty && i < ordered.length) {
        result = ordered(i).find(origin, dir)
        i += 1
      }
      result
  }
}

object Space {
  /** A function for recursively filling a Void with child Voids until a depth of zero is reached, then fill with Volumes.
    *
    * @param depth: The current recursion depth. It can start at any value greater or equal to zero.
    *               When zero is reached, recursion will end.
    *               If a depth of zero is used in the first place, this function will create a leaf bubble.
    * @param position: The position of the bubble that should be created.
    * @param radius: The radius of the current bubble.
    * @param f: A function that will calculate the value of the bubble leaves in the last layer,
    *           taking the position and radius of the bubble as input.
    **/
  def fill[A](depth: Int, position: Vector3, radius: Double, f: (Vector3, Double) => Option[A]): Space[A] = {
    if(depth < 0) {
      throw new Exception("The depth cannot be less than zero")
    }
    else if(depth == 0) {
      Volume(position, radius, f(position, radius))
    }
    else {
      Void(
        position, radius, Array(
          /* Create all diagonal vectors with length of sqrt(2) */
          Vector3( 1d,  1d,  1d),
          Vector3( 1d,  1d, -1d),
          Vector3( 1d, -1d,  1d),
          Vector3( 1d, -1d, -1d),
          Vector3(-1d,  1d,  1d),
          Vector3(-1d,  1d, -1d),
          Vector3(-1d, -1d,  1d),
          Vector3(-1d, -1d, -1d)
        ).map{ v =>
          /* We need to move the children by the length of their radius.
           * The child radius is half the parents radius. */
          position + v.normalized * (0.5 * radius)
        }.map { p =>
          /* Recursively call Space.fill with the matrices we have calculated earlier as input position */
          Space.fill(depth - 1, p, 0.5 * radius, f)
        }
      )
    }
  }
}
