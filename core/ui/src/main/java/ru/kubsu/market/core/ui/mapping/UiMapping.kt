package ru.kubsu.market.core.ui.mapping

import ru.kubsu.market.core.model.*
import ru.kubsu.market.core.ui.model.UiDisplayable
import java.time.LocalDate

fun Employee.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${employeeId ?: "-"} | $fullName"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ФИО" to fullName,
            "Паспорт" to "$passportSeries $passportNumber",
            "Адрес регистрации" to registrationAddress,
            "Дата рождения" to birthDate.toString(),
            "Подразделение" to department,
            "Роль" to (Role.findByRole(role) ?: Role.FIRED).title,
            "Рабочий телефон" to workPhone,
            "Личный телефон" to personalPhone,
            "Email" to email,
        )
}

fun Product.toUiDisplayable() = object : UiDisplayable {
    override val barcode: String?
        get() = this@toUiDisplayable.barcode

    override val displayName: String
        get() = "${productId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Производитель" to manufacturerName,
            "Код производителя" to manufacturerCode,
            "Страна" to manufacturerCountry,
            "Годен до" to LocalDate.now().plusDays(expirationDays).toString(),
            "Размеры" to size,
            "Единица измерения" to unit,
            "Технический срок годности" to expirationDays.toString()
        )
}

fun StorageLocation.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${storageLocationId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ID" to "${storageLocationId ?: "-"}",
            "Название" to name,
            "Тип" to type,
            "Адрес" to address,
        )
}

fun Shelf.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${shelfId ?: "-"} | ${name ?: "Название"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Название" to (name ?: "Название"),
            "Тип" to (type ?: "Обычный")
        )
}

fun Vacation.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${vacationId ?: "-"} | ${type ?: "-"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ID отпуска" to (vacationId?.toString() ?: "-"),
            "ID сотрудника" to (employeeId?.toString() ?: "-"),
            "Тип" to (VacationType.entries.find { it.name.equals(type, true) }
                ?: VacationType.VACATION).title,
            "Дата начала" to (startDate?.toString() ?: "-"),
            "Дата окончания" to (endDate?.toString() ?: "-"),
            "Одобрен" to if (approved) "Да" else "Нет",
            "Рассмотрен" to if (reviewed) "Да" else "Нет"
        )
}

fun PersonnelReport.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${personnelReportId ?: "-"} | ${date?.toString() ?: "—"}"

    override val displayFields: Map<String, String>
        get() = linkedMapOf(
            "Дата" to (date?.toString() ?: "—"),
            "ID директора" to (directorId?.toString() ?: "—"),
            "ID сотрудника" to (employeeId?.toString() ?: "—"),
            "Статус" to (PersonnelReportStatus.entries.find { it.name.equals(status, true) }?.title
                ?: PersonnelReportStatus.DRAFT.title),
            "Всего часов" to (totalHours?.toString() ?: "0"),
            "Переработка" to (overtime?.toString() ?: "0"),
            "Недоработка" to (underwork?.toString() ?: "0"),
            "Комментарий" to (comment ?: "—")
        )
}

fun ContactPerson.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${contactPersonId ?: "-"} | $fullName"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ФИО" to fullName,
            "Контрагент Id" to counterpartyId.toString(),
            "Телефон" to phone,
            "Email" to email,
        )
}

fun Counterparty.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${counterpartyId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Контрагент" to name,
            "Адрес" to address,
            "Контакты" to contactInfo,
        )
}

fun SupplyContract.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${supplyContractId ?: "-"} | $contractorId"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Контрагент Id" to contractorId.toString(),
            "Склад Id" to storageLocationId.toString(),
            "Дата начала" to startDate.toString(),
            "Дата окончания" to endDate.toString(),
        )
}

fun SupplyContractItem.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "Изделие №${supplyContractItemId ?: "-"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Договор Id" to supplyContractId.toString(),
            "Товар Id" to productId.toString(),
            "Количество" to quantity.toString(),
            "Тип доставки" to deliveryType,
        )
}

fun Truck.toUiDisplayable() = object : UiDisplayable {
    override val displayName: String
        get() = "${truckId ?: "-"} | $licencePlate"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Госномер" to licencePlate,
            "Модель" to model,
            "Грузоподъёмность" to capacity.toString(),
            "Водитель Id" to driverId.toString(),
        )
}
