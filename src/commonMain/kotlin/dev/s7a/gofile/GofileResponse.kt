package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * Response types of Gofile.io.
 */
sealed interface GofileResponse<T> {
    /**
     * Response status.
     */
    val status: String

    /**
     * Response data.
     */
    val data: T?

    /**
     * Get whether [status] equals ok.
     */
    val isOk
        get() = status == "ok"

    /**
     * Returns the best server available to receive files.
     *
     * `https://api.gofile.io/getServer`
     */
    @Serializable
    data class GetServer(override val status: String, override val data: Data?) : GofileResponse<GetServer.Data> {
        /**
         * @property server The best server available to receive files.
         */
        @Serializable
        data class Data(val server: String)
    }
}
