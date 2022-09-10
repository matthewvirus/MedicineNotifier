package by.matthewvirus.medicinenotifier.data.datamodel

import java.util.*

data class MedicineDataModel(
    var medicineId: UUID = UUID.randomUUID(),
    var medicineName: String = "",
    var medicineStatus: Boolean = false,
    var medicineNumberInContainer: Int = 0,
    var medicineUseTimesPerDay: String = "",
    var medicineTakingEndDate: Date = Date(),
    var medicineTakingFirstTime: Date = Date(),
    var isEndedForToday: Boolean = false
)