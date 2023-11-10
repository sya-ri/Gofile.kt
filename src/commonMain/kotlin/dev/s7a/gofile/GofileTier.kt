package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * Account tier.
 *
 * @see GofileResponse.GetAccountDetails.tier
 */
@Serializable(with = GofileTierSerializer::class)
public sealed class GofileTier(public open val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return name == (other as GofileTier).name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return name
    }

    /**
     * Guest users.
     */
    public object Guest : GofileTier("guest")

    /**
     * Free plan users.
     */
    public object Standard : GofileTier("standard")

    /**
     * Premium users.
     */
    public object Donor : GofileTier("donor")

    /**
     * Other users.
     */
    public class Unknown(override val name: String) : GofileTier(name)

    public companion object {
        /**
         * Get [GofileTier] from [name].
         */
        public fun from(name: String): GofileTier = when (name) {
            Guest.name -> Guest
            Standard.name -> Standard
            Donor.name -> Donor
            else -> Unknown(name)
        }
    }
}
