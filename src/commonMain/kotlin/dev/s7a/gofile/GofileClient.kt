package dev.s7a.gofile

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json

/**
 * Gofile.io client that uses a pre-setup [HttpClient].
 *
 * @property client [HttpClient]
 */
class GofileClient(private val client: HttpClient) {
    companion object {
        /**
         * Set up [HttpClient] for [GofileClient].
         */
        fun setupClient(client: HttpClientConfig<*>) {
            client.run {
                expectSuccess = true
                install(ContentNegotiation) {
                    json()
                }
            }
        }
    }

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     */
    constructor() : this(HttpClient(Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param engine [HttpClientEngine]
     */
    constructor(engine: HttpClientEngine) : this(HttpClient(engine, Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param factory [HttpClientEngineFactory]
     */
    constructor(factory: HttpClientEngineFactory<*>) : this(factory.create())

    private suspend inline fun <reified T : GofileResponse<R>, R> request(urlString: String, method: HttpMethod): Result<R> {
        return runCatching {
            client.request(urlString) {
                this.method = method
            }
        }.map { response ->
            val body = response.body<T>()
            val data = body.data?.takeIf { body.isOk } ?: return Result.failure(GofileStatusException(response, body.status))
            return Result.success(data)
        }
    }

    /**
     * Get the best server available to receive files.
     *
     * `https://api.gofile.io/getServer`
     *
     * @see getServerName
     */
    suspend fun getServer(): Result<GofileResponse.GetServer.Data> {
        return request<GofileResponse.GetServer, GofileResponse.GetServer.Data>("https://api.gofile.io/getServer", HttpMethod.Get).map { it }
    }

    /**
     * Get the best server name available to receive files.
     *
     * @see getServer
     */
    suspend fun getServerName(): String? {
        return getServer().getOrNull()?.server
    }
}
