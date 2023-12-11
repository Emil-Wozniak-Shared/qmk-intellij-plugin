package pl.ejdev.qmk.rest

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pl.ejdev.qmk.ui.models.keyboard.Keyboard
import pl.ejdev.qmk.ui.models.keyboard.KeyboardInfo
import pl.ejdev.qmk.ui.models.keyboard.Keyboards
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object WebClient {
    private const val HOST = "https://api.ergokey.pl/api"

    private val client: HttpClient = HttpClient.newHttpClient()

    internal fun getKeyboards(): List<Keyboard> {
        val response = client.request<Keyboards>("keyboards")
        return response.keyboards
    }

    internal fun getKeyboard(name: String): Keyboard =
        name.also { println("fetch keyboard '$name'") }
            .runCatching { client.request<Keyboard>("keyboards/$this") }
            .onSuccess { println(it) }
            .onFailure { println(it.message) }
            .getOrElse { Keyboard() }

    internal fun getKeyboardInfo(name: String): KeyboardInfo =
        name.also { println("fetch keyboard info '$name'") }
            .runCatching { client.request<KeyboardInfo>("keyboards/$this/info") }
            .onSuccess { println(it) }
            .onFailure { println(it.message) }
            .getOrElse { KeyboardInfo() }

    private fun endpoint(endpoint: String): HttpRequest = HttpRequest.newBuilder()
        .uri(URI.create("$HOST/$endpoint"))
        .build()

    private inline fun <reified T> HttpClient.request(endpoint: String): T {
        val response = this.send(endpoint(endpoint), HttpResponse.BodyHandlers.ofString())
        val body = response.body()
        return Json.decodeFromString<T>(body)
    }
}

