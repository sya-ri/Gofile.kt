package dev.s7a.gofile

import io.ktor.client.statement.HttpResponse

/**
 * Gofile.io returns some error.
 */
class GofileStatusException(val response: HttpResponse, val status: String) : IllegalStateException() {
    override val message = "GoFile.io Error(${response.call.request.method.value} ${response.call.request.url}: ${response.status}. Status: $status)"
}
