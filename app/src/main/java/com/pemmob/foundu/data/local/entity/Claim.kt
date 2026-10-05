package com.pemmob.foundu.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class ClaimStatus {
    PENDING,
    APPROVED,
    REJECTED
}

@Entity(
    tableName = "claims",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Claim(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val claimantName: String,
    val claimantWhatsapp: String,
    val reason: String,
    val status: ClaimStatus = ClaimStatus.PENDING,
    val claimedAt: Long = System.currentTimeMillis()
)
