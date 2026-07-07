package ru.kubsu.market.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kubsu.market.core.database.entity.VacationEntity

@Dao
interface VacationDao {
    @Query("SELECT * FROM vacations")
    fun getVacations(): Flow<List<VacationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacations(vacations: List<VacationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacation(vacation: VacationEntity)

    @Query("DELETE FROM vacations")
    suspend fun clearAll()
}
