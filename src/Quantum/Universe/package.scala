package Quantum

import Quantum.Math.Immutable.Vector3

package object Universe {

  /** A Volume is a Space with a value, which is an Option */
  case class Volume[A](position: Vector3, radius: Double, value: Option[A]) extends Space[A]

  /** A Void is a Space with an Array of child Spaces, which can be both Volume or Void */
  case class Void[A](position: Vector3, radius: Double, children: Array[Space[A]]) extends Space[A]
}
