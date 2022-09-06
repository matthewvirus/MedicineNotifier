package by.matthewvirus.medicinenotifier.data.datamodel

data class MedicineDataModel(
    var medicineName: String = "",
    var medicineUseTimesPerDay: String = "",
    var medicinePeriodicity: String = "",
    var isEndedForToday: Boolean = false
)