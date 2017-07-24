package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.SleepComponent

class SleepSystem : EntitySystem {
    override val family = EntityFamily.all(SleepComponent::class)

    override fun update(entity: Entity) {
        val sleep = entity[SleepComponent::class]
        if (sleep.sleepTime > 0) {
            sleep.sleepTime--
            if (sleep.sleepTime == 0) {
                entity.post("event.awake")
            }
        }
    }
}
