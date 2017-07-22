package net.gegy1000.hgk.entity

import net.gegy1000.hgk.session.StatusMessage

enum class Pronoun(val personalSubjective: String, val personalObjective: String, val reflexive: String, val possessive: String) {
    NEUTRAL("they", "them", "themself", "their"),
    MALE("he", "him", "himself", "his"),
    FEMALE("she", "her", "herself", "her");

    val properties = arrayOf(
            StatusMessage.Property("pronoun_ps", personalSubjective),
            StatusMessage.Property("pronoun_po", personalObjective),
            StatusMessage.Property("pronoun_rf", reflexive),
            StatusMessage.Property("pronoun_pos", possessive)
    )
}
