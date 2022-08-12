package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * Account tier.
 *
 * @see GofileResponse.GetAccountDetails.tier
 */
@Serializable(with = GofileTierSerializer::class)
sealed class GofileTier(open val name: String) {
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
    object Guest : GofileTier("guest")

    /**
     * Free plan users.
     */
    object Standard : GofileTier("standard")

    /**
     * Subscriber.
     */
    object Donor : GofileTier("donor")

    /**
     * Other users.
     */
    class Unknown(override val name: String) : GofileTier(name)

    companion object {
        /**
         * Get [GofileTier] from [name].
         */
        fun from(name: String) = when (name) {
            Guest.name -> Guest
            Standard.name -> Standard
            Donor.name -> Donor
            else -> Unknown(name)
        }
    }
}
