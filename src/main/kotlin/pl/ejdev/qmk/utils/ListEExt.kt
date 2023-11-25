package pl.ejdev.qmk.utils

private const val NEW_LINE = "\n"
fun List<String>.joinByNewLine() = this.joinToString(NEW_LINE)