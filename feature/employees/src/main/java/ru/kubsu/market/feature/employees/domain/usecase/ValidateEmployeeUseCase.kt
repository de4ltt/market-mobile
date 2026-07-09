package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.Employee
import javax.inject.Inject

class ValidateEmployeeUseCase @Inject constructor() {
    operator fun invoke(employee: Employee): Boolean {
        return employee.fullName.isNotBlank() &&
                employee.passportSeries.isNotBlank() &&
                employee.passportNumber.isNotBlank() &&
                employee.registrationAddress.isNotBlank() &&
                employee.department.isNotBlank() &&
                employee.login.isNotBlank() &&
                employee.password.isNotBlank() &&
                employee.workPhone.isNotBlank() &&
                employee.personalPhone.isNotBlank() &&
                employee.email.isNotBlank()
    }
}
