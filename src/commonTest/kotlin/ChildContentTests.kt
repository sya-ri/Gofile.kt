import dev.s7a.gofile.GofileChildContent
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildContentTests {
    private val json = Json {
        prettyPrint = true
    }

    @Test
    fun encode_file() {
        assertEquals(
            """
                {
                    "id": "_id",
                    "type": "file",
                    "name": "_name",
                    "parentFolder": "_parentFolder",
                    "createTime": 123456789,
                    "size": 123.4,
                    "downloadCount": 12,
                    "md5": "_md5",
                    "mimetype": "_mimetype",
                    "serverChoosen": "_serverChoosen",
                    "directLink": "_directLink",
                    "link": "_link",
                    "thumbnail": "_thumbnail"
                }
            """.trimIndent(),
            json.encodeToString(
                GofileChildContent.serializer(),
                GofileChildContent.File(
                    "_id",
                    "_name",
                    "_parentFolder",
                    123456789,
                    123.4,
                    12,
                    "_md5",
                    "_mimetype",
                    "_serverChoosen",
                    "_directLink",
                    "_link",
                    "_thumbnail"
                )
            )
        )
    }

    @Test
    fun encode_folder() {
        assertEquals(
            """
                {
                    "id": "_id",
                    "type": "folder",
                    "name": "_name",
                    "parentFolder": "_parentFolder",
                    "createTime": 123456789,
                    "childs": [
                    ],
                    "code": "_code",
                    "public": true
                }
            """.trimIndent(),
            json.encodeToString(
                GofileChildContent.serializer(),
                GofileChildContent.Folder(
                    "_id",
                    "_name",
                    "_parentFolder",
                    123456789,
                    listOf(),
                    "_code",
                    public = true
                )
            )
        )
    }

    @Test
    fun encode_folder_with_optional() {
        assertEquals(
            """
                {
                    "id": "_id",
                    "type": "folder",
                    "name": "_name",
                    "parentFolder": "_parentFolder",
                    "createTime": 123456789,
                    "childs": [
                    ],
                    "code": "_code",
                    "public": true,
                    "password": true,
                    "description": "_description",
                    "expire": 987654321,
                    "tags": "t,a,g,s"
                }
            """.trimIndent(),
            json.encodeToString(
                GofileChildContent.serializer(),
                GofileChildContent.Folder(
                    "_id",
                    "_name",
                    "_parentFolder",
                    123456789,
                    listOf(),
                    "_code",
                    public = true,
                    password = true,
                    "_description",
                    987654321,
                    listOf("t", "a", "g", "s")
                )
            )
        )
    }

    @Test
    fun encode_folder_with_childs() {
        assertEquals(
            """
                {
                    "id": "_id",
                    "type": "folder",
                    "name": "_name",
                    "parentFolder": "_parentFolder",
                    "createTime": 123456789,
                    "childs": [
                        "_id1",
                        "_id2",
                        "_id3"
                    ],
                    "code": "_code",
                    "public": true
                }
            """.trimIndent(),
            json.encodeToString(
                GofileChildContent.serializer(),
                GofileChildContent.Folder(
                    "_id",
                    "_name",
                    "_parentFolder",
                    123456789,
                    listOf(
                        "_id1",
                        "_id2",
                        "_id3"
                    ),
                    "_code",
                    public = true
                )
            )
        )
    }

    @Test
    fun decode_file() {
        assertEquals(
            GofileChildContent.File(
                "_id",
                "_name",
                "_parentFolder",
                123456789,
                123.0,
                12,
                "_md5",
                "_mimetype",
                "_serverChoosen",
                "_directLink",
                "_link",
                "_thumbnail"
            ),
            json.decodeFromString(
                GofileChildContent.serializer(),
                """
                    {
                        "id": "_id",
                        "type": "file",
                        "name": "_name",
                        "parentFolder": "_parentFolder",
                        "createTime": 123456789,
                        "size": 123.0,
                        "downloadCount": 12,
                        "md5": "_md5",
                        "mimetype": "_mimetype",
                        "serverChoosen": "_serverChoosen",
                        "directLink": "_directLink",
                        "link": "_link",
                        "thumbnail": "_thumbnail"
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun decode_folder() {
        assertEquals(
            GofileChildContent.Folder(
                "_id",
                "_name",
                "_parentFolder",
                123456789,
                listOf(),
                "_code",
                public = true
            ),
            json.decodeFromString(
                GofileChildContent.serializer(),
                """
                    {
                        "id": "_id",
                        "type": "folder",
                        "name": "_name",
                        "parentFolder": "_parentFolder",
                        "createTime": 123456789,
                        "childs": [
                        ],
                        "code": "_code",
                        "public": true
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun decode_folder_with_optional() {
        assertEquals(
            GofileChildContent.Folder(
                "_id",
                "_name",
                "_parentFolder",
                123456789,
                listOf(),
                "_code",
                public = true,
                password = true,
                "_description",
                987654321,
                listOf("t", "a", "g", "s")
            ),
            json.decodeFromString(
                GofileChildContent.serializer(),
                """
                    {
                        "id": "_id",
                        "type": "folder",
                        "name": "_name",
                        "parentFolder": "_parentFolder",
                        "createTime": 123456789,
                        "childs": [
                        ],
                        "code": "_code",
                        "public": true,
                        "password": true,
                        "description": "_description",
                        "expire": 987654321,
                        "tags": "t,a,g,s"
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun decode_folder_with_childs() {
        assertEquals(
            GofileChildContent.Folder(
                "_id",
                "_name",
                "_parentFolder",
                123456789,
                listOf(
                    "_id1",
                    "_id2",
                    "_id3"
                ),
                "_code",
                public = true
            ),
            json.decodeFromString(
                GofileChildContent.serializer(),
                """
                    {
                        "id": "_id",
                        "type": "folder",
                        "name": "_name",
                        "parentFolder": "_parentFolder",
                        "createTime": 123456789,
                        "childs": [
                            "_id1",
                            "_id2",
                            "_id3"
                        ],
                        "code": "_code",
                        "public": true
                    }
                """.trimIndent()
            )
        )
    }
}
