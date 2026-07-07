package ru.kubsu.market.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kubsu.market.core.database.entity.EmployeeEntity

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees")
    fun getEmployees(): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<EmployeeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Query("DELETE FROM employees WHERE employeeId = :employeeId")
    suspend fun deleteEmployee(employeeId: Int)

    @Query("DELETE FROM employees")
    suspend fun clearAll()
}
