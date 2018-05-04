package Quantum.Graphics

import Quantum.Enrichment._
import java.awt.{Color, Graphics2D}

import Quantum.Math.Mutable.Matrix

import scala.swing.Panel

class Canvas(var matrix: Matrix[Color]) extends Panel {

  override def paintComponent(g: Graphics2D): Unit = {

    val elementWidth = g.getClipBounds.width.f  / matrix.width.f
    val elementHeight = g.getClipBounds.height.f  / matrix.height.f

    Vector.range(0, matrix.width).foreach {
      x => Vector.range(0, matrix.height).foreach {
        y => {
          val x1 = (x * elementWidth).toInt
          val y1 = (y * elementHeight).toInt
          val x2 = ((x + 1) * elementWidth).toInt
          val y2 = ((y + 1) * elementHeight).toInt

          g.setColor(matrix(x, y))
          g.fillRect(x1, y1, x2 - x1, y2 - y1)
        }
      }
    }
  }
}
