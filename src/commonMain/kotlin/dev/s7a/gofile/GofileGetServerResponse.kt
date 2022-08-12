package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * @property server The best server available to receive files.
 */
@Serializable
data class GofileGetServerResponse(val server: String)
