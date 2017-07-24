package net.gegy1000.hgk

fun Boolean?.notNull() = this ?: false

fun Boolean?.orNull() = this ?: true

fun <T: Any> Iterable<T>.typed() = this.map { it::class }
