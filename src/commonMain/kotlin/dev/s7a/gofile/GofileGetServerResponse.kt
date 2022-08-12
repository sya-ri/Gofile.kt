package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * @property server The best server available to receive files.
 * @see GofileClient.getServer
 */
@Serializable
data class GofileGetServerResponse(val server: String)
