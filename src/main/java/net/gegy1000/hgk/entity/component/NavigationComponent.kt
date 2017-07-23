package net.gegy1000.hgk.entity.component

import net.gegy1000.hgk.entity.ai.navigation.Path

class NavigationComponent(val straightCost: Int, val diagonalCost: Int, val upCost: Int) : EntityComponent {
    var target: Target? = null
    var currentPath: Path? = null

    var lastPathRecalculation: Int = 0

    class Target(val x: Int, val y: Int)
}
