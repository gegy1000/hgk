package net.gegy1000.hgk

fun Boolean?.notNull() = this ?: false

fun Boolean?.orNull() = this ?: true
