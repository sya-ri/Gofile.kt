import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.GofileFolderOption
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val fileName = "test.txt"
            val fileContent = """
                Hello!!
                This is a test message.
            """.trimIndent().toByteArray()
            assertTrue(GofileClient(mockEngine).uploadFile(fileName, fileContent, "text/plain", server = "store1").isSuccess)
        }
    }

    @Test
    fun createFolder_should_be_successful() {
        runTest {
            val mockEngine = MockEngine {
                respond(
                    content = """
                        {
                          "status": "ok",
                          "data": {}
                        }
                    """.trimIndent().let(::ByteReadChannel),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            assertTrue(GofileClient(mockEngine).createFolder("aefb20bd-1a19-4194-8c31-e750fbfcf0db", "myFolder", "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM"))
        }
    }

    @Test
    fun createFolder_can_handle_error() {
        runTest {
            val mockEngine = MockEngine {
                respondError(HttpStatusCode.InternalServerError)
            }
            assertFalse(GofileClient(mockEngine).createFolder("aefb20bd-1a19-4194-8c31-e750fbfcf0db", "myFolder", "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM"))
        }
    }

    @Test
    fun setFolderOption_should_be_successful() {
        runTest {
            val mockEngine = MockEngine {
                respond(
                    content = """
                        {
                          "status": "ok",
                          "data": {}
                        }
                    """.trimIndent().let(::ByteReadChannel),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            assertTrue(GofileClient(mockEngine).setFolderOption("aefb20bd-1a19-4194-8c31-e750fbfcf0db", GofileFolderOption.Description("Test+description"), "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM"))
        }
    }

    @Test
    fun setFolderOption_can_handle_error() {
        runTest {
            val mockEngine = MockEngine {
                respondError(HttpStatusCode.InternalServerError)
            }
            assertFalse(GofileClient(mockEngine).setFolderOption("aefb20bd-1a19-4194-8c31-e750fbfcf0db", GofileFolderOption.Description("Test+description"), "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM"))
        }
    }

    @Test
    fun assert_GofileFolderOption() {
        assertEquals("true", GofileFolderOption.Public(true).value)
        assertEquals("false", GofileFolderOption.Public(false).value)
        assertEquals("pass", GofileFolderOption.Password("pass").value)
        assertEquals("abc", GofileFolderOption.Description("abc").value)
        assertEquals("1659636061790", GofileFolderOption.Expire(1659636061790).value)
        assertEquals("abc,def", GofileFolderOption.Tags("abc", "def").value)
    }
}
