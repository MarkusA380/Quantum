package Quantum

import scala.language.implicitConversions

package object Enrichment {

  implicit class EnrichInt(i: Int) {
    def f: Float = i.toFloat
    def d: Double = i.toDouble
  }

  implicit class EnrichDouble(d: Double) {
    def f: Float = d.toFloat
  }

  implicit class EnrichLong(l: Long) {
    def f: Float = l.toFloat
    def d: Double = l.toDouble
    def i: Int = l.toInt
  }
}
