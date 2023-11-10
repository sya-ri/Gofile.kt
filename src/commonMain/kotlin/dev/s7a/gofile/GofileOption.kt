package dev.s7a.gofile

/**
 * The value of the option to be defined.
 *
 * @property name Can be "public", "password", "description", "expire", "tags" or "directLink".
 */
sealed class GofileOption(val name: String) {
    /**
     * The option value.
     */
    abstract val value: String

    /**
     * Whether anyone can access it.
     */
    data class Public(val isPublic: Boolean) : GofileOption("public") {
        override val value = isPublic.toString()
    }

    /**
     * Password is required for access.
     */
    data class Password(override val value: String) : GofileOption("password")

    /**
     * Description.
     */
    data class Description(override val value: String) : GofileOption("description")

    /**
     * Expiration date in the form of unix timestamp.
     */
    data class Expire(val timestamp: Long) : GofileOption("expire") {
        override val value = timestamp.toString()
    }

    /**
     * Tags.
     */
    data class Tags(val values: List<String>) : GofileOption("tags") {
        constructor(vararg value: String) : this(value.toList())

        override val value = values.joinToString(",")
    }

    /**
     * The contentId must be a file.
     */
    data class DirectLink(val directLink: Boolean) : GofileOption("directLink") {
        override val value = directLink.toString()
    }
}
