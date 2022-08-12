import dev.s7a.gofile.GofileTier
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
            GofileTier.Donor,
            Json.decodeFromString(
                GofileTier.serializer(),
                """
                    "donor"
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
            Json.encodeToString(GofileTier.serializer(), GofileTier.Guest)
        )
        assertEquals(
            """
                "standard"
            """.trimIndent(),
            Json.encodeToString(GofileTier.serializer(), GofileTier.Standard)
        )
        assertEquals(
            """
                "donor"
            """.trimIndent(),
            Json.encodeToString(GofileTier.serializer(), GofileTier.Donor)
        )
        assertEquals(
            """
                "other"
            """.trimIndent(),
            Json.encodeToString(GofileTier.serializer(), GofileTier.Unknown("other"))
        )
    }
}
