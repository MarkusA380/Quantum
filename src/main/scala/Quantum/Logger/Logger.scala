package Quantum

import java.io.OutputStream

import scala.collection.mutable.ArrayBuffer

package object Logger {

  private var debug = false

  def enableDebug(debug: Boolean) = {
    this.debug = debug
  }

  def debug(message: String): Unit = {
    if(debug) println(s"[DEBUG] $message")
  }

  def info(message: String): Unit = {
    println(s"[INFO] $message")
  }

  def error(message: String, exit: Boolean = true): Unit = {
    println(s"[ERROR] $message")
    if(exit) System.exit(1)
  }

  val outputStream: OutputStream = new OutputStream {

    val buffer = new ArrayBuffer[Char](0)

    override def write(b: Int): Unit = if(b.toChar == '\n') {
      val s = buffer.map(_.toString).reduceLeft((s, s1) => s + s1)
      buffer.clear()
      error(s, exit = false)
    } else {
      buffer += b.toChar
    }
  }
}
