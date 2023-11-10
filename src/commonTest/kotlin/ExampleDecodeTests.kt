import dev.s7a.gofile.GofileChildContent
import dev.s7a.gofile.GofileContent
import dev.s7a.gofile.GofileGetAccountDetailsResponse
import dev.s7a.gofile.GofileGetServerResponse
import dev.s7a.gofile.GofileResponse
import dev.s7a.gofile.GofileTier
import dev.s7a.gofile.GofileUploadFileResponse
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Last update: 2023-04-20
 */
class ExampleDecodeTests {
    @Test
    fun getServer() {
        assertEquals(
            GofileResponse.Ok(
                GofileGetServerResponse(
                    server = "store1",
                ),
            ),
            Json.decodeFromString(
                GofileResponse.serializer(GofileGetServerResponse.serializer()),
                """
                    {
                      "status": "ok",
                      "data": {
                        "server": "store1"
                      }
                    }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun uploadFile() {
        assertEquals(
            GofileResponse.Ok(
                GofileUploadFileResponse(
                    downloadPage = "https://gofile.io/d/Z19n9a",
                    code = "Z19n9a",
                    parentFolder = "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                    fileId = "4991e6d7-5217-46ae-af3d-c9174adae924",
                    fileName = "example.mp4",
                    md5 = "10c918b1d01aea85864ee65d9e0c2305",
                ),
            ),
            Json.decodeFromString(
                GofileResponse.serializer(GofileUploadFileResponse.serializer()),
                """
                    {
                      "status": "ok",
                      "data": {
                        "downloadPage": "https://gofile.io/d/Z19n9a",
                        "code": "Z19n9a",
                        "parentFolder": "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                        "fileId": "4991e6d7-5217-46ae-af3d-c9174adae924",
                        "fileName": "example.mp4",
                        "md5": "10c918b1d01aea85864ee65d9e0c2305"
                      }
                    }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun getContent() {
        assertEquals(
            GofileResponse.Ok(
                GofileContent.Folder(
                    isOwner = true,
                    id = "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                    name = "Z19n9a",
                    parentFolder = "3241d27a-f7e1-4158-bc75-73d057eff5fa",
                    code = "Z19n9a",
                    createTime = 1648229689,
                    public = true,
                    childs = listOf(
                        "4991e6d7-5217-46ae-af3d-c9174adae924",
                    ),
                    totalDownloadCount = 0,
                    totalSize = 9840497.0,
                    contents = mapOf(
                        "4991e6d7-5217-46ae-af3d-c9174adae924" to GofileChildContent.File(
                            id = "4991e6d7-5217-46ae-af3d-c9174adae924",
                            name = "example.mp4",
                            parentFolder = "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                            createTime = 1648229689,
                            size = 9840497.0,
                            downloadCount = 0,
                            md5 = "10c918b1d01aea85864ee65d9e0c2305",
                            mimetype = "video/mp4",
                            serverChoosen = "store4",
                            directLink = "https://store4.gofile.io/download/direct/4991e6d7-5217-46ae-af3d-c9174adae924/example.mp4",
                            link = "https://store4.gofile.io/download/4991e6d7-5217-46ae-af3d-c9174adae924/example.mp4",
                        ),
                    ),
                ),
            ),
            Json.decodeFromString(
                GofileResponse.serializer(GofileContent.serializer()),
                """
                    {
                      "status": "ok",
                      "data": {
                        "isOwner": true,
                        "id": "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                        "type": "folder",
                        "name": "Z19n9a",
                        "parentFolder": "3241d27a-f7e1-4158-bc75-73d057eff5fa",
                        "code": "Z19n9a",
                        "createTime": 1648229689,
                        "public": true,
                        "childs": [
                          "4991e6d7-5217-46ae-af3d-c9174adae924"
                        ],
                        "totalDownloadCount": 0,
                        "totalSize": 9840497,
                        "contents": {
                          "4991e6d7-5217-46ae-af3d-c9174adae924": {
                            "id": "4991e6d7-5217-46ae-af3d-c9174adae924",
                            "type": "file",
                            "name": "example.mp4",
                            "parentFolder": "3dbc2f87-4c1e-4a81-badc-af004e61a5b4",
                            "createTime": 1648229689,
                            "size": 9840497,
                            "downloadCount": 0,
                            "md5": "10c918b1d01aea85864ee65d9e0c2305",
                            "mimetype": "video/mp4",
                            "serverChoosen": "store4",
                            "directLink": "https://store4.gofile.io/download/direct/4991e6d7-5217-46ae-af3d-c9174adae924/example.mp4",
                            "link": "https://store4.gofile.io/download/4991e6d7-5217-46ae-af3d-c9174adae924/example.mp4"
                          }
                        }
                      }
                    }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun getAccountDetails() {
        assertEquals(
            GofileResponse.Ok(
                GofileGetAccountDetailsResponse(
                    token = "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM",
                    email = "email@domain.tld",
                    tier = GofileTier.Standard,
                    rootFolder = "2aecea58-84e6-420d-b2b9-68b4add8418d",
                    foldersCount = 4,
                    filesCount = 20,
                    totalSize = 67653500.0,
                    totalDownloadCount = 1,
                ),
            ),
            Json.decodeFromString(
                GofileResponse.serializer(GofileGetAccountDetailsResponse.serializer()),
                """
                    {
                      "status": "ok",
                      "data": {
                        "token": "ivlW1ZSGn2Y4AoADbCHUjllj2cO9m3WM",
                        "email": "email@domain.tld",
                        "tier": "standard",
                        "rootFolder": "2aecea58-84e6-420d-b2b9-68b4add8418d",
                        "foldersCount": 4,
                        "filesCount": 20,
                        "totalSize": 67653500,
                        "totalDownloadCount": 1
                      }
                    }
                """.trimIndent(),
            ),
        )
    }
}
