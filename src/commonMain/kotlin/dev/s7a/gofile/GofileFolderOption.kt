package dev.s7a.gofile

/**
 * The value of the option to be defined.
 *
 * @property name Can be "public", "password", "description", "expire" or "tags".
 */
sealed class GofileFolderOption(val name: String) {
    /**
     * The option value.
     */
    abstract val value: String

    /**
     * Whether anyone can access it.
     */
    data class Public(val isPublic: Boolean) : GofileFolderOption("public") {
        override val value = isPublic.toString()
    }

    /**
     * Password is required for access.
     */
    data class Password(override val value: String) : GofileFolderOption("password")

    /**
     * Description.
     */
    data class Description(override val value: String) : GofileFolderOption("description")

    /**
     * Expiration date in the form of unix timestamp.
     */
    data class Expire(val timestamp: Long) : GofileFolderOption("expire") {
        override val value = timestamp.toString()
    }

    /**
     * Tags.
     */
    data class Tags(val values: List<String>) : GofileFolderOption("tags") {
        constructor(vararg value: String) : this(value.toList())

        override val value = values.joinToString(",")
    }
}
