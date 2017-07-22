package net.gegy1000.hgk.entity

import java.util.Random

data class PlayerStatistics(
        val aggresivity: Float,
        val likability: Float,
        val friendliness: Float,
        val motivation: Float,
        val speed: Float,
        val strength: Float,
        val meleeSkill: Float,
        val rangedSkill: Float,
        val explosiveSkill: Float,
        val plantKnowledge: Float,
        val craftSkill: Float,
        val firstAidSkill: Float
) {
    companion object {
        fun random(random: Random) = PlayerStatistics(
                aggresivity = random.nextFloat(),
                likability = random.nextFloat(),
                friendliness = random.nextFloat(),
                motivation = random.nextFloat(),
                speed = random.nextFloat() * 0.5F + 0.5F,
                strength = random.nextFloat(),
                meleeSkill = random.nextFloat(),
                rangedSkill = random.nextFloat(),
                explosiveSkill = random.nextFloat(),
                plantKnowledge = random.nextFloat(),
                craftSkill = random.nextFloat(),
                firstAidSkill = random.nextFloat()
        )
    }
}
