package Quantum.Utils

object Color {

  implicit class EnrichIntTuple(color: (Int, Int, Int)) {
    def toColor: Int = color match {
      case (r: Int, g: Int, b: Int) =>
        (((r << 8) + g) << 8) + b
    }
  }

  implicit class EnrichDoubleTuple(color: (Double, Double, Double)) {
    def toColor: Int = color match {
      case (r: Double, g: Double, b: Double) =>
        ((r * 0xff).toInt, (g * 0xff).toInt, (b * 0xff).toInt).toColor
    }
  }

  implicit class EnrichInt(color: Int) {

    def toRGBd(max: Double = 1.0): (Double, Double, Double) = {
      def resize(i: Int): Double = (i / 255.0) * max

      (
        resize(color >> 16),
        resize((color - ((color >> 16) << 16)) >> 8),
        resize(color - ((color >> 8) << 8))
      )
    }

    def toRGBi(max: Int = 255): (Int, Int, Int) = {
      def resize(i: Int): Int = ((i / 255.0) * max).toInt

      (
        resize(color >> 16),
        resize((color - ((color >> 16) << 16)) >> 8),
        resize(color - ((color >> 8) << 8))
      )
    }
  }

  def random: Int = (white * randomDouble).toInt

  val black = 0x000000

  val white = 0xffffff
}
