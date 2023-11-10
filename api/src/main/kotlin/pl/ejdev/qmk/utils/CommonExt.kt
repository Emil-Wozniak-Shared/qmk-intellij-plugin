package pl.ejdev.qmk.utils

fun<T> T?.orRaise(message: String): T = this ?: error(message)