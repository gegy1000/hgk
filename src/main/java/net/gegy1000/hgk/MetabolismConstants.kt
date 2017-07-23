package net.gegy1000.hgk

object MetabolismConstants {
    const val IDLE_ENERGY_USAGE = 1
    const val MOVING_ENERGY_USAGE = 2

    const val IDLE_WATER_USAGE = 1
    const val MOVING_WATER_USAGE = 2

    const val IDLE_STAMINA_INCREMENT = 4
    const val MOVING_STAMINA_DECREMENT = -1

    /**
     * 2 days while moving, 4 days while still
     */
    const val MAX_FOOD = TimerConstants.TICKS_PER_DAY * MOVING_ENERGY_USAGE * 2

    /**
     * 1 day while moving, 2 days while still
     */
    const val MAX_WATER = TimerConstants.TICKS_PER_DAY * MOVING_WATER_USAGE

    /**
     * 2 hours to drain, 30 minutes to refill
     */
    const val MAX_STAMINA = TimerConstants.TICKS_PER_HOUR * -MOVING_STAMINA_DECREMENT * 2

    /**
     * Rest for 30 minutes minimum
     */
    const val MIN_REST_TIME = TimerConstants.TICKS_PER_HOUR / 2

    const val WATER_PROCESS_SPEED = 3
    const val FOOD_PROCESS_SPEED = 5

    /**
     * 4 harvests = 1 day moving
     */
    const val HARVEST_FOOD_INCREMENT = (MAX_FOOD / 2) / 4

    /**
     * 5 drinks = 1 day moving
     */
    const val DRINK_WATER_INCREMENT = MAX_WATER / 5
}
