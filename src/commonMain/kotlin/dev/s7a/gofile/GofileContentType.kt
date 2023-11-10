package dev.s7a.gofile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * File types.
 */
@Serializable
public enum class GofileContentType {
    /**
     * File.
     */
    @SerialName("file")
    File,

    /**
     * Folder.
     */
    @SerialName("folder")
    Folder
}
