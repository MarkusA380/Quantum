package Quantum.Universe

import Quantum.Math.{Sphere, Vector3}
import Quantum.Enrichment.EnrichNumerical
import Quantum.Utils.Color.EnrichDoubleTuple

object Figures {
  val constructs: Map[String, Sphere => Boolean] = Map (
    "sphere" -> {
      s: Sphere => s.position.length < 0.5
    },
    "torus2" -> {
      s: Sphere => {
        0.01 <= (
          s.position * 2 match {
            case Vector3(z, y, x) => ((x.sq + y.sq).sq - x.sq + y.sq).sq + z.sq
          }
        )
      }
    }
  )

  val textures: Map[String, Vector3 => Int] = Map(
    "rainbow" -> {
      pos: Vector3 => {
        def f(d: Double) = (math.sin(d * 5) + 1) / 2
        (f(pos.x), f(pos.y), f(pos.z)).toColor
      }
    },
    "checkered" -> {
      pos: Vector3 => {
        def f(d: Double) = (math.round((math.sin(d * 5) + 1) / 2) + 1) / 2.d
        (f(pos.x), f(pos.y), f(pos.z)).toColor
      }
    }
  )
}
