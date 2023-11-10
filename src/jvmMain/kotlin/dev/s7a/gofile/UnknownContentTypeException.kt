package dev.s7a.gofile

import java.io.File

/**
 * The content type could not be determined.
 * Check the file or specify the contentType.
 */
public class UnknownContentTypeException(public val file: File) : IllegalArgumentException() {
    override val message: String = "The content type could not be determined: ${file.name}. Check the file or specify the contentType."
}
