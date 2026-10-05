package com.pemmob.foundu.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class ItemStatus(val displayName: String) {
    tersedia("Tersedia"),
    diverifikasi("Diverifikasi"),
    terklaim("Terklaim")
}

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = PickupPoint::class,
            parentColumns = ["id"],
            childColumns = ["pickupPointId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val status: ItemStatus = ItemStatus.tersedia,
    val categoryId: Long?,
    val pickupPointId: Long?,
    val locationFound: String = "",
    val reportedBy: String = "",
    val reportedAt: Long = System.currentTimeMillis()
)
