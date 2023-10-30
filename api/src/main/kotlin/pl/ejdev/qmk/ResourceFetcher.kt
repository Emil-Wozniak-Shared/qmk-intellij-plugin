package pl.ejdev.qmk

import java.net.URL

object ResourceFetcher {
    private val loader = this.javaClass.getClassLoader()
    fun getResource(base: String,  name: String?): URL? {
        return loader.getResource(name).also {
            println(it)
        }
    }
}
