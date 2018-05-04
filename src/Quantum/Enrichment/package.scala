package Quantum

import scala.language.implicitConversions

package object Enrichment {

  implicit class EnrichInt(i: Int) {
    def f = i.toFloat
    def d = i.toDouble
  }

  implicit class EnrichDouble(d: Double) {
    def f = d.toFloat
  }

  implicit class EnrichLong(l: Long) {
    def f = l.toFloat
    def d = l.toDouble
    def i = l.toInt
  }
}
