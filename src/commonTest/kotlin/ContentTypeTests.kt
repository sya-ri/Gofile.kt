import dev.s7a.gofile.GofileContentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ContentTypeTests {
    @Test
    fun encode() {
        assertEquals(
            """
                "file"
            """.trimIndent(),
            Json.encodeToString(GofileContentType.File),
        )
        assertEquals(
            """
                "folder"
            """.trimIndent(),
            Json.encodeToString(GofileContentType.Folder),
        )
    }

    @Test
    fun decode() {
        assertEquals(
            GofileContentType.File,
            Json.decodeFromString(
                """
                    "file"
                """.trimIndent(),
            ),
        )
        assertEquals(
            GofileContentType.Folder,
            Json.decodeFromString(
                """
                    "folder"
                """.trimIndent(),
            ),
        )
    }
}
