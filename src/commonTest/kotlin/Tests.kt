import dev.s7a.gofile.GofileClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class Tests {
    @Test
    fun getServer_should_be_successful() {
        runTest {
            val mockEngine = MockEngine {
                respond(
                    content = """
                        {
                          "status": "ok",
                          "data": {
                            "server": "store1"
                          }
                        }
                    """.trimIndent().let(::ByteReadChannel),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            assertEquals("store1", GofileClient(mockEngine).getServerName())
        }
    }

    @Test
    fun getServer_can_handle_error() {
        runTest {
            val mockEngine = MockEngine {
                respondError(HttpStatusCode.InternalServerError)
            }
            assertNull(GofileClient(mockEngine).getServerName())
        }
    }
}
