package dev.s7a.gofile

import kotlinx.serialization.Serializable

@Serializable(with = GofileContentSerializer::class)
sealed class GofileContent {
    /**
     * Content type.
     */
    abstract val type: GofileContentType

    /**
     * File.
     */
    object File : GofileContent() {
        override val type = GofileContentType.File
    }

    /**
     * Folder.
     *
     * @property isOwner Whether the user is the owner of the folder.
     * @property id id of the folder.
     * @property name Name of the folder.
     * @property parentFolder id of the prent folder.
     * @property code Code of the folder.
     * @property createTime Create time.
     * @property public Whether anyone can access it.
     * @property childs File or folder id list.
     * @property totalDownloadCount Total number of downloads.
     * @property totalSize Total size of childs.
     * @property contents List of files or folders with [childs] as key.
     */
    data class Folder(
        val isOwner: Boolean,
        val id: String,
        val name: String,
        val parentFolder: String,
        val code: String,
        val createTime: Long,
        val public: Boolean,
        val childs: List<String>,
        val totalDownloadCount: Int,
        val totalSize: Double,
        val contents: Map<String, GofileChildContent>
    ) : GofileContent() {
        init {
            require(childs.size == contents.size && childs.toSet() == contents.keys) {
                "childs and contents must have the same contents."
            }
        }

        override val type = GofileContentType.Folder
    }
}
