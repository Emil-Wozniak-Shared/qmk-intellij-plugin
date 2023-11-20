package pl.ejdev.qmk.utils

import java.io.File

fun File.nameWithout(ext: String = ".json"): String = name.replace(ext, "")
