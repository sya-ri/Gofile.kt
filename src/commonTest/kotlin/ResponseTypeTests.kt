import dev.s7a.gofile.GofileResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTypeTests {
    @Test
    fun encode_GofileResponse_ok() {
        assertEquals(
            """
                {"status":"ok","data":{"server":"_server"}}
            """.trimIndent(),
            Json.encodeToString<GofileResponse<GofileResponse.GetServer>>(GofileResponse.Ok(GofileResponse.GetServer("_server")))
        )
    }

    @Test
    fun encode_GofileResponse_error() {
        assertEquals(
            """
                {"status":"_status"}
            """.trimIndent(),
            Json.encodeToString<GofileResponse<GofileResponse.GetServer>>(GofileResponse.Error("_status"))
        )
    }

    @Test
    fun encode_GofileResponseOk() {
        assertEquals(
            """
                {"data":{"server":"_server"}}
            """.trimIndent(),
            Json.encodeToString(GofileResponse.Ok(GofileResponse.GetServer("_server")))
        )
    }

    @Test
    fun encode_GofileResponseError() {
        assertEquals(
            """
                {"status":"_status"}
            """.trimIndent(),
            Json.encodeToString(GofileResponse.Error("_status"))
        )
    }

    @Test
    fun decode_GofileResponse_ok() {
        assertEquals(
            GofileResponse.Ok(GofileResponse.GetServer("_server")),
            """
                {"status":"ok","data":{"server":"_server"}}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileResponse.GetServer>>(it)
            }
        )
    }

    @Test
    fun decode_GofileResponse_error() {
        assertEquals(
            GofileResponse.Error("_status"),
            """
                {"status":"_status"}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileResponse.GetServer>>(it)
            }
        )
    }

    @Test
    fun decode_GofileResponse_error_including_empty_data() {
        assertEquals(
            GofileResponse.Error("_status"),
            """
                {"status":"_status","data":{}}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileResponse.GetServer>>(it)
            }
        )
    }

    @Test
    fun decode_GofileResponseOk() {
        assertEquals(
            GofileResponse.Ok(GofileResponse.GetServer("_server")),
            """
                {"data":{"server":"_server"}}
            """.trimIndent().let {
                Json.decodeFromString(it)
            }
        )
    }

    @Test
    fun decode_GofileResponseError() {
        assertEquals(
            GofileResponse.Error("_status"),
            """
                {"status":"_status"}
            """.trimIndent().let {
                Json.decodeFromString(it)
            }
        )
    }
}
