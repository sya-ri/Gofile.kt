import dev.s7a.gofile.GofileGetServerResponse
import dev.s7a.gofile.GofileResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTypeTests {
    @Test
    fun encode_ok() {
        assertEquals(
            """
                {"status":"ok","data":{"server":"_server"}}
            """.trimIndent(),
            Json.encodeToString<GofileResponse<GofileGetServerResponse>>(GofileResponse.Ok(GofileGetServerResponse("_server")))
        )
    }

    @Test
    fun encode_error() {
        assertEquals(
            """
                {"status":"_status"}
            """.trimIndent(),
            Json.encodeToString<GofileResponse<GofileGetServerResponse>>(GofileResponse.Error("_status"))
        )
    }

    @Test
    fun encode_error_including_data() {
        assertEquals(
            """
                {"status":"_status","data":{"a":"1","b":"2"}}
            """.trimIndent(),
            Json.encodeToString<GofileResponse<GofileGetServerResponse>>(GofileResponse.Error("_status", JsonObject(mapOf("a" to JsonPrimitive("1"), "b" to JsonPrimitive("2")))))
        )
    }

    @Test
    fun decode_ok() {
        assertEquals(
            GofileResponse.Ok(GofileGetServerResponse("_server")),
            """
                {"status":"ok","data":{"server":"_server"}}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileGetServerResponse>>(it)
            }
        )
    }

    @Test
    fun decode_error() {
        assertEquals(
            GofileResponse.Error("_status"),
            """
                {"status":"_status"}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileGetServerResponse>>(it)
            }
        )
    }

    @Test
    fun decode_error_including_empty_data() {
        assertEquals(
            GofileResponse.Error("_status", JsonObject(emptyMap())),
            """
                {"status":"_status","data":{}}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileGetServerResponse>>(it)
            }
        )
    }

    @Test
    fun decode_error_including_data() {
        assertEquals(
            GofileResponse.Error("_status", JsonObject(mapOf("a" to JsonPrimitive("1"), "b" to JsonPrimitive("2")))),
            """
                {"status":"_status","data":{"a":"1","b":"2"}}
            """.trimIndent().let {
                Json.decodeFromString<GofileResponse<GofileGetServerResponse>>(it)
            }
        )
    }
}
