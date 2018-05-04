package Quantum

import scala.util.Random

package object Utils {
  val startTime = System.currentTimeMillis

  def time = System.currentTimeMillis - startTime

  def random: Double = Random.nextDouble
}
