package dev.s7a.gofile

import io.ktor.http.HttpMethod

/**
 * Request types of Gofile.io.
 */
sealed interface GofileRequest {
    /**
     * Http method.
     */
    val method: HttpMethod

    /**
     * Request url.
     */
    val urlString: String

    /**
     * Returns the best server available to receive files.
     */
    object GetServer : GofileRequest {
        override val method = HttpMethod.Get
        override val urlString = "https://api.gofile.io/getServer"
    }
}
