package Quantum

import scala.util.Random

package object Utils {

  val startTime: Long = System.currentTimeMillis

  def currentTime: Long = System.currentTimeMillis - startTime

  def randomDouble: Double = Random.nextDouble

  def randomInt: Int = Random.nextInt
}
