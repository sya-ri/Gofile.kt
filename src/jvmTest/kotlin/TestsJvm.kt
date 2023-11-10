import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.uploadFile
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TestsJvm {
    @Test
    fun uploadFile_should_be_successful() {
        runTest {
            val mockEngine = MockEngine {
                // TODO: Assert body?
                respond(
                    content = """
                        {
                          "status": "ok",
                          "data": {
                            "downloadPage": "https://gofile.io/d/Z19n9a",
                            "code": "Z19n9a",
                            "parentFolder": "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                            "fileId": "4991e6d7-5217-46ae-af3d-c9174adae924",
                            "fileName": "test.txt",
                            "md5": "10c918b1d01aea85864ee65d9e0c2305"
                          }
                        }
                    """.trimIndent().let(::ByteReadChannel),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val directory = createTempDirectory()
            val file = directory.resolve("test.txt").toFile()
            file.writeText(
                """
                    Hello!!
                    This is a test message.
                """.trimIndent(),
            )
            assertTrue(GofileClient(mockEngine).uploadFile(file, "text/plain", server = "store1").isSuccess)
        }
    }
}
