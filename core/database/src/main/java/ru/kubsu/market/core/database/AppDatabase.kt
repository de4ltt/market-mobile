package ru.kubsu.market.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kubsu.market.core.database.converter.Converters
import ru.kubsu.market.core.database.dao.EmployeeDao
import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.core.database.entity.EmployeeEntity
import ru.kubsu.market.core.database.entity.VacationEntity

@Database(
    entities = [
        EmployeeEntity::class,
        VacationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun vacationDao(): VacationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "market_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
