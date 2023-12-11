package pl.ejdev.qmk.service

import com.intellij.openapi.components.PersistentStateComponent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jdom.Content
import org.jdom.Element
import org.jdom.Text
import pl.ejdev.qmk.ui.utils.io.json.toJson
import java.util.stream.Stream

abstract class BasePersistStateElement : PersistentStateComponent<Element> {
    fun loadState() = loadState(currentState)

    protected abstract var currentState: Element

    protected inline fun <reified T> decode(body: String) = Json.decodeFromString<T>(body)

    protected inline fun <reified T : Any> textCollection(
        name: String,
        elements: Collection<T>
    ) = Element(name, name, name).apply {
        elements.map { it.toJson() }.map { Text(it) }.let { this.addContent(it) }
    }

    protected fun Stream<Content>.extract(action: (Pair<String, String>) -> Unit) {
        this.forEach {
            action(it.namespacesIntroduced.first().uri to it.value)
        }
    }
}
