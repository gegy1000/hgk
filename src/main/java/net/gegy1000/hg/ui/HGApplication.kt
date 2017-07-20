package net.gegy1000.hg.ui

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.stage.Stage
import net.gegy1000.hg.HGSimulator
import net.gegy1000.hg.arena.Arena

class HGApplication : Application() {
    override fun start(stage: Stage) {
        HGSimulator.startLoop()

        val canvas = Canvas(WIDTH, HEIGHT)

        RenderHandler.graphics = canvas.graphicsContext2D
        RenderHandler.canvas = canvas

        val pane = Pane(canvas)

        canvas.widthProperty().bind(pane.widthProperty())
        canvas.heightProperty().bind(pane.heightProperty())

        stage.scene = Scene(pane, WIDTH, HEIGHT)

        stage.setOnCloseRequest {
            Platform.exit()
            System.exit(0)
        }

        stage.title = "Hunger Games Simulation"

        stage.width = WIDTH
        stage.height = HEIGHT
        stage.isResizable = false
        stage.show()

        RenderHandler.start()
    }

    object RenderHandler : AnimationTimer() {
        lateinit var graphics: GraphicsContext
        lateinit var canvas: Canvas

        override fun handle(now: Long) {
            graphics.clearRect(0.0, 0.0, canvas.width, canvas.height)

            graphics.drawImage(HGSimulator.groundcoverImage, 0.0, 0.0, canvas.height, canvas.height)

            HGSimulator.entities.forEach {
                val x = it.tileX / Arena.SIZE.toDouble() * canvas.height
                val y = it.tileY / Arena.SIZE.toDouble() * canvas.height
                graphics.fillRect(x - 2, y - 2, 4.0, 4.0)
            }
        }
    }

    companion object {
        const val WIDTH = 920.0
        const val HEIGHT = 540.0

        @JvmStatic
        fun main(args: Array<String>) {
            launch(HGApplication::class.java)
        }
    }
}
