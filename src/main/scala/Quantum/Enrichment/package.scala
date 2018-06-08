package Quantum

import scala.language.implicitConversions

package object Enrichment {

  implicit class EnrichNumerical[A](a: A)(implicit num: Numeric[A] ) {
    def sq: A = num.times(a, a)

    def f: Float = num.toFloat(a)
    def d: Double = num.toDouble(a)
    def i: Int = num.toInt(a)
    def l: Long = num.toLong(a)
  }
}
