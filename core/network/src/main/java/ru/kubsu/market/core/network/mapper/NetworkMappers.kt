package ru.kubsu.market.core.network.mapper

import ru.kubsu.market.core.model.*
import ru.kubsu.market.core.model.pricing.*
import ru.kubsu.market.core.network.dto.*

// ConfirmReportRequest Mappers
fun ConfirmReportRequestDto.toDomain(): ConfirmReportRequest = ConfirmReportRequest(
    directorComment = directorComment,
    manualOvertime = manualOvertime,
    manualUnderwork = manualUnderwork
)

fun ConfirmReportRequest.toDto(): ConfirmReportRequestDto = ConfirmReportRequestDto(
    directorComment = directorComment,
    manualOvertime = manualOvertime,
    manualUnderwork = manualUnderwork
)

// Position Mappers
fun PositionDto.toDomain(): Position = Position(
    positionId = positionId,
    name = name,
    description = description,
    monthlyHours = monthlyHours,
    salaryRate = salaryRate
)

fun Position.toDto(): PositionDto = PositionDto(
    positionId = positionId,
    name = name,
    description = description,
    monthlyHours = monthlyHours,
    salaryRate = salaryRate
)

// Employee Mappers
fun EmployeeDto.toDomain(): Employee = Employee(
    employeeId = employeeId,
    fullName = fullName,
    passportSeries = passportSeries,
    passportNumber = passportNumber,
    registrationAddress = registrationAddress,
    birthDate = birthDate,
    position = position.toDomain(),
    department = department,
    login = login,
    password = password,
    role = role,
    workPhone = workPhone,
    personalPhone = personalPhone,
    email = email
)

fun Employee.toDto(): EmployeeDto = EmployeeDto(
    employeeId = employeeId,
    fullName = fullName,
    passportSeries = passportSeries,
    passportNumber = passportNumber,
    registrationAddress = registrationAddress,
    birthDate = birthDate,
    position = position.toDto(),
    department = department,
    login = login,
    password = password,
    role = role,
    workPhone = workPhone,
    personalPhone = personalPhone,
    email = email
)

// PersonnelReport Mappers
fun PersonnelReportDto.toDomain(): PersonnelReport = PersonnelReport(
    personnelReportId = personnelReportId,
    date = date,
    directorId = directorId,
    employeeId = employeeId,
    status = status,
    totalHours = totalHours,
    overtime = overtime,
    underwork = underwork,
    comment = comment
)

fun PersonnelReport.toDto(): PersonnelReportDto = PersonnelReportDto(
    personnelReportId = personnelReportId,
    date = date,
    directorId = directorId,
    employeeId = employeeId,
    status = status,
    totalHours = totalHours,
    overtime = overtime,
    underwork = underwork,
    comment = comment
)

// Product Mappers
fun ProductDto.toDomain(): Product = Product(
    productId = productId,
    name = name,
    manufacturerName = manufacturerName,
    manufacturerCountry = manufacturerCountry,
    manufacturerCode = manufacturerCode,
    size = size,
    unit = unit,
    barcode = barcode,
    additionalInfo = additionalInfo,
    storageRequirement = storageRequirement
)

fun Product.toDto(): ProductDto = ProductDto(
    productId = productId,
    name = name,
    manufacturerName = manufacturerName,
    manufacturerCountry = manufacturerCountry,
    manufacturerCode = manufacturerCode,
    size = size,
    unit = unit,
    barcode = barcode,
    additionalInfo = additionalInfo,
    storageRequirement = storageRequirement
)

// ProductPrice Mappers
fun ProductPriceDto.toDomain(): ProductPrice = ProductPrice(
    productId = productId,
    productName = productName,
    currentPrice = currentPrice,
    regularPrice = regularPrice,
    labelType = labelType,
    effectiveDate = effectiveDate
)

fun ProductPrice.toDto(): ProductPriceDto = ProductPriceDto(
    productId = productId,
    productName = productName,
    currentPrice = currentPrice,
    regularPrice = regularPrice,
    labelType = labelType,
    effectiveDate = effectiveDate
)

// StorageLocation Mappers
fun StorageLocationDto.toDomain(): StorageLocation = StorageLocation(
    storageLocationId = storageLocationId,
    name = name,
    type = type,
    address = address
)

fun StorageLocation.toDto(): StorageLocationDto = StorageLocationDto(
    storageLocationId = storageLocationId,
    name = name,
    type = type,
    address = address
)

// Shelf Mappers
fun ShelfDto.toDomain(): Shelf = Shelf(
    shelfId = shelfId,
    name = name,
    storageLocation = storageLocation?.toDomain(),
    type = type
)

fun Shelf.toDto(): ShelfDto = ShelfDto(
    shelfId = shelfId,
    name = name,
    storageLocation = storageLocation?.toDto(),
    type = type
)

// ReceivedProduct Mappers
fun ReceivedProductDto.toDomain(): ReceivedProduct = ReceivedProduct(
    receivedProductId = receivedProductId,
    employeeId = employeeId,
    product = product.toDomain(),
    date = date,
    status = status,
    quantity = quantity,
    expirationDate = expirationDate,
    comment = comment
)

fun ReceivedProduct.toDto(): ReceivedProductDto = ReceivedProductDto(
    receivedProductId = receivedProductId,
    employeeId = employeeId,
    product = product.toDto(),
    date = date,
    status = status,
    quantity = quantity,
    expirationDate = expirationDate,
    comment = comment
)

// Vacation Mappers
fun VacationDto.toDomain(): Vacation = Vacation(
    vacationId = vacationId,
    employeeId = employeeId,
    type = type,
    startDate = startDate,
    endDate = endDate,
    approved = approved,
    reviewed = reviewed
)

fun Vacation.toDto(): VacationDto = VacationDto(
    vacationId = vacationId,
    employeeId = employeeId,
    type = type,
    startDate = startDate,
    endDate = endDate,
    approved = approved,
    reviewed = reviewed
)

// WorkDay Mappers
fun WorkDayDto.toDomain(): WorkDay = WorkDay(
    workDayId = workDayId,
    employeeId = employeeId,
    date = date,
    checkIn = checkIn,
    checkOut = checkOut,
    hoursWorked = hoursWorked,
    overtime = overtime,
    underwork = underwork
)

fun WorkDay.toDto(): WorkDayDto = WorkDayDto(
    workDayId = workDayId,
    employeeId = employeeId,
    date = date,
    checkIn = checkIn,
    checkOut = checkOut,
    hoursWorked = hoursWorked,
    overtime = overtime,
    underwork = underwork
)

// PriceFormationResult Mappers
fun PriceFormationResultDto.ProductPriceDto.toDomain(): PriceFormationResult.ProductPrice = PriceFormationResult.ProductPrice(
    productId = productId,
    regularPrice = regularPrice,
    appliedPrice = appliedPrice,
    priceTagType = priceTagType.toDomain(),
    source = source?.toDomain(),
    discountPercent = discountPercent
)

fun PriceFormationResultDto.PriceSourceDto.toDomain(): PriceFormationResult.PriceSource = PriceFormationResult.PriceSource(
    kind = kind.toDomain(),
    priceListId = priceListId,
    comment = comment
)

fun PriceFormationResultDto.PriceListKindDto.toDomain(): PriceFormationResult.PriceListKind = when (this) {
    PriceFormationResultDto.PriceListKindDto.REGULAR -> PriceFormationResult.PriceListKind.REGULAR
    PriceFormationResultDto.PriceListKindDto.MARKDOWN -> PriceFormationResult.PriceListKind.MARKDOWN
    PriceFormationResultDto.PriceListKindDto.PROMO -> PriceFormationResult.PriceListKind.PROMO
}

fun PriceFormationResultDto.PriceTagTypeDto.toDomain(): PriceFormationResult.PriceTagType = when (this) {
    PriceFormationResultDto.PriceTagTypeDto.WHITE -> PriceFormationResult.PriceTagType.WHITE
    PriceFormationResultDto.PriceTagTypeDto.YELLOW -> PriceFormationResult.PriceTagType.YELLOW
    PriceFormationResultDto.PriceTagTypeDto.ACTION -> PriceFormationResult.PriceTagType.ACTION
}

fun PriceFormationResultDto.PriceTagToPrintDto.toDomain(): PriceFormationResult.PriceTagToPrint = PriceFormationResult.PriceTagToPrint(
    productId = productId,
    priceTagType = priceTagType.toDomain(),
    regularPrice = regularPrice,
    appliedPrice = appliedPrice
)

fun PriceFormationResultDto.CouponToPrintDto.toDomain(): PriceFormationResult.CouponToPrint = PriceFormationResult.CouponToPrint(
    couponId = couponId,
    productId = productId,
    receivedProductId = receivedProductId,
    expirationDate = expirationDate,
    discountType = discountType.toDomain(),
    discountValue = discountValue,
    finalPrice = finalPrice
)

fun PriceFormationResultDto.DiscountTypeDto.toDomain(): PriceFormationResult.DiscountType = when (this) {
    PriceFormationResultDto.DiscountTypeDto.PERCENT -> PriceFormationResult.DiscountType.PERCENT
    PriceFormationResultDto.DiscountTypeDto.AMOUNT -> PriceFormationResult.DiscountType.AMOUNT
}

fun PriceFormationResultDto.StopListItemDto.toDomain(): PriceFormationResult.StopListItem = PriceFormationResult.StopListItem(
    productId = productId,
    reason = reason.toDomain(),
    proposedPrice = proposedPrice,
    limits = limits?.toDomain(),
    comment = comment
)

fun PriceFormationResultDto.LimitsDto.toDomain(): PriceFormationResult.Limits = PriceFormationResult.Limits(
    dailyChangeMaxPercent = dailyChangeMaxPercent,
    maxMarkupPercent = maxMarkupPercent
)

fun PriceFormationResultDto.StopReasonDto.toDomain(): PriceFormationResult.StopReason = when (this) {
    PriceFormationResultDto.StopReasonDto.DAILY_CHANGE_LIMIT_EXCEEDED -> PriceFormationResult.StopReason.DAILY_CHANGE_LIMIT_EXCEEDED
    PriceFormationResultDto.StopReasonDto.MARKUP_CAP_EXCEEDED -> PriceFormationResult.StopReason.MARKUP_CAP_EXCEEDED
    PriceFormationResultDto.StopReasonDto.INVALID_INPUT_PRICE -> PriceFormationResult.StopReason.INVALID_INPUT_PRICE
    PriceFormationResultDto.StopReasonDto.OTHER -> PriceFormationResult.StopReason.OTHER
}

fun PriceFormationResultDto.toDomain(): PriceFormationResult = PriceFormationResult(
    formedAt = formedAt,
    productPrices = productPrices.map { it.toDomain() },
    couponsToPrint = couponsToPrint.map { it.toDomain() },
    priceTagsToPrint = priceTagsToPrint.map { it.toDomain() },
    stopList = stopList.map { it.toDomain() },
    warnings = warnings
)
