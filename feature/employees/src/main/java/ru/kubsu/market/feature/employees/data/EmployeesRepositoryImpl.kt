package ru.kubsu.market.feature.employees.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kubsu.market.core.database.dao.EmployeeDao
import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.core.database.mapper.toDomain
import ru.kubsu.market.core.database.mapper.toEntity
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.network.api.EmployeesApi
import ru.kubsu.market.core.network.mapper.toDomain
import ru.kubsu.market.core.network.mapper.toDto
import ru.kubsu.market.feature.employees.domain.EmployeesRepository
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor(
    private val employeesApi: EmployeesApi,
    private val employeeDao: EmployeeDao,
    private val vacationDao: VacationDao
) : EmployeesRepository {

    override fun getEmployeesFlow(): Flow<List<Employee>> {
        return employeeDao.getEmployees().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getVacationsFlow(): Flow<List<Vacation>> {
        return vacationDao.getVacations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshEmployees() {
        val remoteEmployees = employeesApi.getEmployees().map { it.toDomain() }
        val entities = remoteEmployees.map { it.toEntity() }
        employeeDao.clearAll()
        employeeDao.insertEmployees(entities)
    }

    override suspend fun refreshVacations() {
        val remoteVacations = employeesApi.getVacations().map { it.toDomain() }
        val entities = remoteVacations.map { it.toEntity() }
        vacationDao.clearAll()
        vacationDao.insertVacations(entities)
    }

    override suspend fun getPositions(): List<Position> {
        return employeesApi.getPositions().map { it.toDomain() }
    }

    override suspend fun addEmployee(employee: Employee) {
        val added = employeesApi.addEmployee(employee.toDto()).toDomain()
        employeeDao.insertEmployee(added.toEntity())
    }

    override suspend fun deleteEmployee(employeeId: Int) {
        employeesApi.deleteEmployee(employeeId)
        employeeDao.deleteEmployee(employeeId)
    }

    override suspend fun requestVacation(vacation: Vacation) {
        val created = employeesApi.requestVacation(vacation.toDto()).toDomain()
        vacationDao.insertVacation(created.toEntity())
    }

    override suspend fun respondToVacation(vacationId: Int, approve: Boolean) {
        val updated = employeesApi.respondToVacation(vacationId, approve).toDomain()
        vacationDao.insertVacation(updated.toEntity())
    }

    override suspend fun getEmployeeProfile(employeeId: Int): Employee {
        return employeesApi.getEmployeeProfile(employeeId).toDomain()
    }
}
