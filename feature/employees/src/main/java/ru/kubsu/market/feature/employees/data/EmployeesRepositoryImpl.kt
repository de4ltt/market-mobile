package ru.kubsu.market.feature.employees.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kubsu.market.core.database.dao.EmployeeDao
import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.core.database.entity.EmployeeEntity
import ru.kubsu.market.core.database.entity.VacationEntity
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

class EmployeesRepositoryImpl(
    private val httpClient: HttpClient,
    private val employeeDao: EmployeeDao,
    private val vacationDao: VacationDao
) : EmployeesRepository {

    private val baseUrl = ru.kubsu.market.core.network.ApiConfig.BASE_URL

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
        val remoteEmployees = httpClient.get("$baseUrl/employees").body<List<Employee>>()
        val entities = remoteEmployees.map { EmployeeEntity.fromDomain(it) }
        employeeDao.clearAll()
        employeeDao.insertEmployees(entities)
    }

    override suspend fun refreshVacations() {
        val remoteVacations = httpClient.get("$baseUrl/vacations").body<List<Vacation>>()
        val entities = remoteVacations.map { VacationEntity.fromDomain(it) }
        vacationDao.clearAll()
        vacationDao.insertVacations(entities)
    }

    override suspend fun getPositions(): List<Position> {
        return httpClient.get("$baseUrl/positions").body<List<Position>>()
    }

    override suspend fun addEmployee(employee: Employee) {
        val added = httpClient.post("$baseUrl/employees") {
            contentType(ContentType.Application.Json)
            setBody(employee)
        }.body<Employee>()
        employeeDao.insertEmployee(EmployeeEntity.fromDomain(added))
    }

    override suspend fun deleteEmployee(employeeId: Int) {
        httpClient.delete("$baseUrl/employees/$employeeId")
        employeeDao.deleteEmployee(employeeId)
    }

    override suspend fun requestVacation(vacation: Vacation) {
        val created = httpClient.post("$baseUrl/vacations") {
            contentType(ContentType.Application.Json)
            setBody(vacation)
        }.body<Vacation>()
        vacationDao.insertVacation(VacationEntity.fromDomain(created))
    }
}
