package dev.s7a.gofile

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Response types of Gofile.io.
 */
@Serializable(with = GofileResponseSerializer::class)
internal sealed class GofileResponse<out T> {
    /**
     * Gofile.io returns "ok" status.
     */
    data class Ok<out T>(val data: T) : GofileResponse<T>()

    /**
     * Gofile.io returns some error.
     */
    data class Error(val status: String, val data: JsonObject? = null) : GofileResponse<Nothing>()
}
