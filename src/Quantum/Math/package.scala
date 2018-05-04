package Quantum

package object Math {
  final case class MatrixOperationException(private val message: String, private val cause: Throwable = None.orNull) extends Exception(message, cause)
}
