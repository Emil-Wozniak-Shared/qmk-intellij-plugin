package pl.ejdev.qmk.rest

import com.intellij.idea.IdeaLogger
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val API_URL = "https://api.ergokey.pl/api"

private val log: IdeaLogger = IdeaLogger.getInstance("WebClient") as IdeaLogger
private val client: HttpClient = HttpClient.newHttpClient()

internal inline fun <reified T : Any> request(path: String, message: String): T =
    log.warn(message)
        .runCatching { client.request<T>(path) }
        .onSuccess { log.warn(it.toString()) }
        .onFailure { log.error(it.message) }
        .getOrElse {
            T::class.java.constructors.first().newInstance() as T
        }

private fun endpoint(endpoint: String): HttpRequest = HttpRequest.newBuilder()
    .uri(URI.create("${API_URL}/$endpoint"))
    .build()

private inline fun <reified T> HttpClient.request(endpoint: String): T {
    val response = this.send(endpoint(endpoint), HttpResponse.BodyHandlers.ofString())
    val body = response.body()
    return Json.decodeFromString<T>(body)
}
