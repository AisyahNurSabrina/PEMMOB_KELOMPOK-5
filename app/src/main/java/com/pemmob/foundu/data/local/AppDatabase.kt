package com.pemmob.foundu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pemmob.foundu.data.local.dao.CategoryDao
import com.pemmob.foundu.data.local.dao.ClaimDao
import com.pemmob.foundu.data.local.dao.ItemDao
import com.pemmob.foundu.data.local.dao.PickupPointDao
import com.pemmob.foundu.data.local.entity.Category
import com.pemmob.foundu.data.local.entity.Claim
import com.pemmob.foundu.data.local.entity.ClaimStatus
import com.pemmob.foundu.data.local.entity.Item
import com.pemmob.foundu.data.local.entity.ItemStatus
import com.pemmob.foundu.data.local.entity.PickupPoint

class Converters {
    @TypeConverter
    fun fromItemStatus(status: ItemStatus): String = status.name

    @TypeConverter
    fun toItemStatus(value: String): ItemStatus = 
        try { ItemStatus.valueOf(value) } catch (e: Exception) { ItemStatus.tersedia }

    @TypeConverter
    fun fromClaimStatus(status: ClaimStatus): String = status.name

    @TypeConverter
    fun toClaimStatus(value: String): ClaimStatus = ClaimStatus.valueOf(value)
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE items ADD COLUMN locationFound TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE items ADD COLUMN reportedBy TEXT NOT NULL DEFAULT ''")
    }
}

@Database(
    entities = [Category::class, PickupPoint::class, Item::class, Claim::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun pickupPointDao(): PickupPointDao
    abstract fun itemDao(): ItemDao
    abstract fun claimDao(): ClaimDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foundu_db"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
