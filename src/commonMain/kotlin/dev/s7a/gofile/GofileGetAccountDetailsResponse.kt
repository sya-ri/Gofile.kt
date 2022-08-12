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
 * @property filesCountLimit Limit of [filesCount].
 * @property totalSize A size of all files.
 * @property totalSizeLimit Limit of [totalSize].
 * @property total30DDLTraffic DDL traffic increases when someone downloads your content through a direct link. It is counted for the last 30 days. Downloads from the website are unlimited.
 * @property total30DDLTrafficLimit Limit of [total30DDLTraffic].
 * @see GofileClient.getAccountDetails
 */
@Serializable
data class GofileGetAccountDetailsResponse(
    val token: String,
    val email: String,
    val tier: GofileTier,
    val tierAmount: Int? = null,
    val rootFolder: String,
    val filesCount: Int,
    val filesCountLimit: Int?,
    val totalSize: Double,
    val totalSizeLimit: Double?,
    val total30DDLTraffic: Double,
    val total30DDLTrafficLimit: Double?
)
