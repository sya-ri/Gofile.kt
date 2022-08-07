import dev.s7a.gofile.GofileClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.http.decodeURLPart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun assertUrl(expected: String, block: suspend GofileClient.() -> Unit) {
    runTest {
        var actual: String? = null
        val mockEngine = MockEngine {
            actual = it.url.toString().decodeURLPart()
            respondBadRequest()
        }
        GofileClient(mockEngine).block()
        assertEquals(expected, actual)
    }
}
