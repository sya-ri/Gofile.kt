import dev.s7a.gofile.GofileChildContent
import dev.s7a.gofile.GofileContent
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ContentTests {
    private val json = Json {
        prettyPrint = true
    }

    @Test
    fun encode_file() {
        assertEquals(
            """
                "not-a-folder"
            """.trimIndent(),
            json.encodeToString(GofileContent.serializer(), GofileContent.File)
        )
    }

    @Test
    fun encode_folder() {
        assertEquals(
            """
                {
                    "isOwner": true,
                    "id": "_id",
                    "type": "folder",
                    "name": "_",
                    "parentFolder": "_parentFolder",
                    "code": "_code",
                    "createTime": 123456789,
                    "public": true,
                    "childs": [
                    ],
                    "totalDownloadCount": 12,
                    "totalSize": 123.4,
                    "contents": {
                    }
                }
            """.trimIndent(),
            json.encodeToString(
                GofileContent.serializer(),
                GofileContent.Folder(
                    true,
                    "_id",
                    "_",
                    "_parentFolder",
                    "_code",
                    123456789,
                    true,
                    listOf(),
                    12,
                    123.4,
                    mapOf()
                )
            )
        )
    }

    @Test
    fun encode_folder_with_childs() {
        assertEquals(
            """
                {
                    "isOwner": true,
                    "id": "_id",
                    "type": "folder",
                    "name": "_",
                    "parentFolder": "_parentFolder",
                    "code": "_code",
                    "createTime": 123456789,
                    "public": true,
                    "childs": [
                        "_id1",
                        "_id2",
                        "_id3"
                    ],
                    "totalDownloadCount": 12,
                    "totalSize": 123.4,
                    "contents": {
                        "_id1": {
                            "id": "_id1",
                            "type": "folder",
                            "name": "_name1",
                            "parentFolder": "_parentFolder1",
                            "createTime": 12345678,
                            "childs": [
                                "_id11",
                                "_id12",
                                "_id13"
                            ],
                            "code": "_code1",
                            "public": false
                        },
                        "_id2": {
                            "id": "_id2",
                            "type": "file",
                            "name": "_name2",
                            "parentFolder": "_parentFolder2",
                            "createTime": 1234567,
                            "size": 1234.5,
                            "downloadCount": 123,
                            "md5": "_md5",
                            "mimetype": "_mimetype2",
                            "serverChoosen": "_serverChoosen2",
                            "directLink": "_directLink2",
                            "link": "_link2"
                        },
                        "_id3": {
                            "id": "_id3",
                            "type": "folder",
                            "name": "_name3",
                            "parentFolder": "_parentFolder3",
                            "createTime": 123456,
                            "childs": [
                            ],
                            "code": "_code3",
                            "public": true,
                            "password": true,
                            "description": "_description3",
                            "expire": 12345,
                            "tags": "t,a,g,s,3"
                        }
                    }
                }
            """.trimIndent(),
            json.encodeToString(
                GofileContent.serializer(),
                GofileContent.Folder(
                    true,
                    "_id",
                    "_",
                    "_parentFolder",
                    "_code",
                    123456789,
                    true,
                    listOf(
                        "_id1",
                        "_id2",
                        "_id3"
                    ),
                    12,
                    123.4,
                    mapOf(
                        "_id1" to GofileChildContent.Folder(
                            "_id1",
                            "_name1",
                            "_parentFolder1",
                            12345678,
                            listOf(
                                "_id11",
                                "_id12",
                                "_id13"
                            ),
                            "_code1",
                            false
                        ),
                        "_id2" to GofileChildContent.File(
                            "_id2",
                            "_name2",
                            "_parentFolder2",
                            1234567,
                            1234.5,
                            123,
                            "_md5",
                            "_mimetype2",
                            "_serverChoosen2",
                            "_directLink2",
                            "_link2"
                        ),
                        "_id3" to GofileChildContent.Folder(
                            "_id3",
                            "_name3",
                            "_parentFolder3",
                            123456,
                            listOf(),
                            "_code3",
                            public = true,
                            password = true,
                            "_description3",
                            12345,
                            "t,a,g,s,3"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun decode_file() {
        assertEquals(
            GofileContent.File,
            Json.decodeFromString(
                GofileContent.serializer(),
                """
                    "not-a-folder"
                """.trimIndent()
            )
        )
    }

    @Test
    fun decode_folder() {
        assertEquals(
            GofileContent.Folder(
                true,
                "_id",
                "_",
                "_parentFolder",
                "_code",
                123456789,
                true,
                listOf(),
                12,
                123.0,
                mapOf()
            ),
            json.decodeFromString(
                GofileContent.serializer(),
                """
                    {
                        "isOwner": true,
                        "id": "_id",
                        "type": "folder",
                        "name": "_",
                        "parentFolder": "_parentFolder",
                        "code": "_code",
                        "createTime": 123456789,
                        "public": true,
                        "childs": [
                        ],
                        "totalDownloadCount": 12,
                        "totalSize": 123.0,
                        "contents": {
                        }
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun decode_folder_with_childs() {
        assertEquals(
            GofileContent.Folder(
                true,
                "_id",
                "_",
                "_parentFolder",
                "_code",
                123456789,
                true,
                listOf(
                    "_id1",
                    "_id2",
                    "_id3"
                ),
                12,
                123.0,
                mapOf(
                    "_id1" to GofileChildContent.Folder(
                        "_id1",
                        "_name1",
                        "_parentFolder1",
                        12345678,
                        listOf(
                            "_id11",
                            "_id12",
                            "_id13"
                        ),
                        "_code1",
                        false
                    ),
                    "_id2" to GofileChildContent.File(
                        "_id2",
                        "_name2",
                        "_parentFolder2",
                        1234567,
                        1234.0,
                        123,
                        "_md5",
                        "_mimetype2",
                        "_serverChoosen2",
                        "_directLink2",
                        "_link2",
                        "_thumbnail2"
                    ),
                    "_id3" to GofileChildContent.Folder(
                        "_id3",
                        "_name3",
                        "_parentFolder3",
                        123456,
                        listOf(),
                        "_code3",
                        public = true,
                        password = true,
                        "_description3",
                        12345,
                        "t,a,g,s,3"
                    )
                )
            ),
            json.decodeFromString(
                GofileContent.serializer(),
                """
                    {
                        "isOwner": true,
                        "id": "_id",
                        "type": "folder",
                        "name": "_",
                        "parentFolder": "_parentFolder",
                        "code": "_code",
                        "createTime": 123456789,
                        "public": true,
                        "childs": [
                            "_id1",
                            "_id2",
                            "_id3"
                        ],
                        "totalDownloadCount": 12,
                        "totalSize": 123.0,
                        "contents": {
                            "_id1": {
                                "id": "_id1",
                                "type": "folder",
                                "name": "_name1",
                                "parentFolder": "_parentFolder1",
                                "createTime": 12345678,
                                "childs": [
                                    "_id11",
                                    "_id12",
                                    "_id13"
                                ],
                                "code": "_code1",
                                "public": false
                            },
                            "_id2": {
                                "id": "_id2",
                                "type": "file",
                                "name": "_name2",
                                "parentFolder": "_parentFolder2",
                                "createTime": 1234567,
                                "size": 1234.0,
                                "downloadCount": 123,
                                "md5": "_md5",
                                "mimetype": "_mimetype2",
                                "serverChoosen": "_serverChoosen2",
                                "directLink": "_directLink2",
                                "link": "_link2",
                                "thumbnail": "_thumbnail2"
                            },
                            "_id3": {
                                "id": "_id3",
                                "type": "folder",
                                "name": "_name3",
                                "parentFolder": "_parentFolder3",
                                "createTime": 123456,
                                "childs": [
                                ],
                                "code": "_code3",
                                "public": true,
                                "password": true,
                                "description": "_description3",
                                "expire": 12345,
                                "tags": "t,a,g,s,3"
                            }
                        }
                    }
                """.trimIndent()
            )
        )
    }
}
