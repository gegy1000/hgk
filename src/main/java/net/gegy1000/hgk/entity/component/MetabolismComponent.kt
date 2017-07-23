package net.gegy1000.hgk.entity.component

import net.gegy1000.hgk.MetabolismConstants

class MetabolismComponent : EntityComponent {
    val hunger: Float
        get() = 1.0F - (energy + foodProcessing * 0.8F) / MetabolismConstants.MAX_FOOD.toFloat()

    val thirst: Float
        get() = 1.0F - (hydration + waterProcessing * 0.8F) / MetabolismConstants.MAX_WATER.toFloat()

    // TODO: More realistic start values + noise
    var hydration = MetabolismConstants.MAX_WATER / 2/*(MetabolismConstants.MAX_WATER * 1.8).toInt()*/
    var energy = MetabolismConstants.MAX_FOOD / 2/*(MetabolismConstants.MAX_FOOD * 1.8).toInt()*/
    var stamina = MetabolismConstants.MAX_STAMINA

    var foodProcessing = 0
    var waterProcessing = 0
}
