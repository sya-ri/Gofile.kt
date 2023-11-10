package dev.s7a.gofile

import io.ktor.client.statement.HttpResponse

/**
 * Gofile.io returns some error.
 */
public class GofileStatusException(public val response: HttpResponse, public val status: String) : IllegalStateException() {
    override val message: String = "GoFile.io Error(${response.call.request.method.value} ${response.call.request.url}: ${response.status}. Status: $status)"
}
