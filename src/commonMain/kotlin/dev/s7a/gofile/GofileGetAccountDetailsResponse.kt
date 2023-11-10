package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * **The statistics are updated every 24 hours.**
 *
 * @property token The access token of an account.
 * @property email The email of an account.
 * @property tier The tier of an account.
 * @property tierAmount Dollars paid monthly.
 * @property rootFolder The root folder id.
 * @property filesCount A number of files.
 * @property foldersCount
 * @property totalSize A size of all files.
 * @property totalDownloadCount
 * @see GofileClient.getAccountDetails
 */
@Serializable
public data class GofileGetAccountDetailsResponse(
    val token: String,
    val email: String,
    val tier: GofileTier,
    val tierAmount: Int? = null,
    val rootFolder: String,
    val filesCount: Int,
    val foldersCount: Int? = null,
    val totalSize: Double,
    val totalDownloadCount: Int? = null,
)
