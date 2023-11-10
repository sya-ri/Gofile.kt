package dev.s7a.gofile

/**
 * The value of the option to be defined.
 *
 * @property name Can be "public", "password", "description", "expire", "tags" or "directLink".
 */
public sealed class GofileOption(public val name: String) {
    /**
     * The option value.
     */
    public abstract val value: String

    /**
     * Whether anyone can access it.
     */
    public data class Public(val isPublic: Boolean) : GofileOption("public") {
        override val value: String = isPublic.toString()
    }

    /**
     * Password is required for access.
     */
    public data class Password(override val value: String) : GofileOption("password")

    /**
     * Description.
     */
    public data class Description(override val value: String) : GofileOption("description")

    /**
     * Expiration date in the form of unix timestamp.
     */
    public data class Expire(val timestamp: Long) : GofileOption("expire") {
        override val value: String = timestamp.toString()
    }

    /**
     * Tags.
     */
    public data class Tags(val values: List<String>) : GofileOption("tags") {
        public constructor(vararg value: String) : this(value.toList())

        override val value: String = values.joinToString(",")
    }

    /**
     * The contentId must be a file.
     */
    public data class DirectLink(val directLink: Boolean) : GofileOption("directLink") {
        override val value: String = directLink.toString()
    }
}
