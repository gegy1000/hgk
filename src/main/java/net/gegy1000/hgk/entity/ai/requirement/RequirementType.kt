package net.gegy1000.hgk.entity.ai.requirement

enum class RequirementType(val requirement: PlayerRequirement) {
    FIND_FOOD(FindFoodRequirement),
    FIND_WATER(FindWaterRequirement),
    REST(RestRequirement);

    companion object {
        val values = RequirementType.values().map(RequirementType::requirement)
    }
}
