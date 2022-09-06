package by.matthewvirus.medicinenotifier.data.datamodel

import java.util.*

data class PatientDataModel(
    var patientId: UUID = UUID.randomUUID(),
    var patientName: String = "",
    var patientSurname: String = "",
    var prescribedMedicines: List<MedicineDataModel>,
    var patientStatus: Boolean = false
)