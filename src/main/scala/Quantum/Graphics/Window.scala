package Quantum.Graphics

import java.io.PrintStream

import Quantum.Math.Vector2
import Quantum.Utils.Color.EnrichInt
import Quantum.{Logger, Utils}
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

class Window(title: String, width: Int, height: Int, frame: Frame, shader: (Int, Int, Long) => Int) {

  GLFWErrorCallback.createPrint(new PrintStream(Logger.outputStream)).set()

  if (!glfwInit) {
    Logger.error("Unable to initialize GLFW.")
  } else {
    Logger.debug("Successfully initialized GLFW.")
  }

  glfwDefaultWindowHints()
  glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
  glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

  val window: Long = glfwCreateWindow(width, height, title, NULL, NULL)

  if (window == NULL) {
    Logger.error("Failed to create window.")
  } else {
    Logger.debug("Successfully created window.")
  }

  glfwSetKeyCallback(
    window,
    (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {

      Logger.debug(s"KeyCallback: [$key, $scancode, $action, $mods]")

      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        Logger.info("Quitting application.")
        glfwSetWindowShouldClose(window, true)
      }
    }
  )

  private val stack = stackPush
  try {
    val pWidth = stack.mallocInt(1)
    val pHeight = stack.mallocInt(1)

    glfwGetWindowSize(window, pWidth, pHeight)

    val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor)

    val posX = (videoMode.width - pWidth.get(0)) / 2
    val posY = (videoMode.height - pHeight.get(0)) / 2

    Logger.debug(s"Setting window position to $posX, $posY.")

    glfwSetWindowPos(window, posX, posY)

  } finally stack.close()

  glfwMakeContextCurrent(window)
  glfwSwapInterval(1)

  Logger.debug("Showing window.")
  glfwShowWindow(window)


  def loop(): Unit = {
    Logger.debug("Running main loop...")
    GL.createCapabilities()

    glClearColor(1f, 0f, 0f, 0f)

    while(!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      /* Use GLFW to get the window size */
      val widthBuf = stack.mallocInt(1)
      val heightBuf = stack.mallocInt(1)

      glfwGetWindowSize(window, widthBuf, heightBuf)

      val width = widthBuf.get(0)
      val height = heightBuf.get(0)

      glMatrixMode(GL_PROJECTION)
      glLoadIdentity()
      glViewport(0, 0, width, height)
      glOrtho(0, frame.width, frame.height, 0, 0, 1)

      /* Update the shader */
      val t = Utils.currentTime
      frame.update(shader(_, _, t), 4)

      /* Repaint the updated frame */
      for(x <- 0 until frame.width; y <- 0 until frame.height) {

        /* Here we have to use Eta Expansion to convert the method glColor3i to a function */
        val color = frame(x, y)
        (glColor3i _).tupled(color.toRGBi(Int.MaxValue))

        val corner1 = Vector2(x, y)
        val corner2 = Vector2(x + 1, y + 1)
        glRectd(corner1.x, corner1.y, corner2.x, corner2.y)
      }

      glfwSwapBuffers(window)
      glfwPollEvents()
    }
    Logger.debug("Exit main loop.")
  }

  def destroy(): Unit = {
    Logger.debug("Closing window.")
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}
