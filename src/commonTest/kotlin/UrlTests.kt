import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.GofileFolderOption
import kotlin.test.Test

class UrlTests {
    @Test
    fun expected_getServer_url() {
        assertUrl("https://api.gofile.io/getServer", GofileClient::getServer)
    }

    @Test
    fun expected_uploadFile_url() {
        assertUrl("https://_server.gofile.io/uploadFile?token=_token&folderId=_folderId") {
            uploadFile("", byteArrayOf(), "", "_token", "_folderId", "_server")
        }
    }

    @Test
    fun expected_createFolder_url() {
        assertUrl("https://api.gofile.io/createFolder?parentFolderId=_parentFolderId&folderName=_folderName&token=_token") {
            createFolder("_parentFolderId", "_folderName", "_token")
        }
    }

    @Test
    fun expected_setFolderOption_url() {
        assertUrl("https://api.gofile.io/setFolderOption?folderId=_folderId&option=public&value=false&token=_token") {
            setFolderOption("_folderId", GofileFolderOption.Public(false), "_token")
        }
        assertUrl("https://api.gofile.io/setFolderOption?folderId=_folderId&option=password&value=_password&token=_token") {
            setFolderOption("_folderId", GofileFolderOption.Password("_password"), "_token")
        }
        assertUrl("https://api.gofile.io/setFolderOption?folderId=_folderId&option=description&value=_description&token=_token") {
            setFolderOption("_folderId", GofileFolderOption.Description("_description"), "_token")
        }
        assertUrl("https://api.gofile.io/setFolderOption?folderId=_folderId&option=expire&value=1234567890&token=_token") {
            setFolderOption("_folderId", GofileFolderOption.Expire(1234567890), "_token")
        }
        assertUrl("https://api.gofile.io/setFolderOption?folderId=_folderId&option=tags&value=t,a,g,s&token=_token") {
            setFolderOption("_folderId", GofileFolderOption.Tags("t", "a", "g", "s"), "_token")
        }
    }
}
