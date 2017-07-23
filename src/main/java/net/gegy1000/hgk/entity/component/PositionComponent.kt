package net.gegy1000.hgk.entity.component

class PositionComponent(var x: Double, var y: Double) : EntityComponent {
    var lastX: Double = x
    var lastY: Double = y

    val tileX: Int
        get() = x.toInt()
    val tileY: Int
        get() = y.toInt()

    fun distance(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY)
    }
}
