import dev.s7a.gofile.GofileTier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class TierTests {
    @Test
    fun decode() {
        assertEquals(
            GofileTier.Guest,
            Json.decodeFromString(
                GofileTier.serializer(),
                """
                    "guest"
                """.trimIndent()
            )
        )
        assertEquals(
            GofileTier.Standard,
            Json.decodeFromString(
                GofileTier.serializer(),
                """
                    "standard"
                """.trimIndent()
            )
        )
        assertEquals(
            GofileTier.Unknown("other"),
            Json.decodeFromString(
                GofileTier.serializer(),
                """
                    "other"
                """.trimIndent()
            )
        )
    }

    @Test
    fun encode() {
        assertEquals(
            """
                "guest"
            """.trimIndent(),
            Json.encodeToString<GofileTier>(GofileTier.Guest)
        )
        assertEquals(
            """
                "standard"
            """.trimIndent(),
            Json.encodeToString<GofileTier>(GofileTier.Standard)
        )
        assertEquals(
            """
                "other"
            """.trimIndent(),
            Json.encodeToString<GofileTier>(GofileTier.Unknown("other"))
        )
    }
}
