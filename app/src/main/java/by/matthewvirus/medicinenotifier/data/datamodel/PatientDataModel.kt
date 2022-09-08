package by.matthewvirus.medicinenotifier.data.datamodel

import java.util.*

data class PatientDataModel(
    var patientId: UUID = UUID.randomUUID(),
    var patientName: String = "",
    var patientSurname: String = "",
    var patientPatronymic: String = "",
    var patientDiagnosis: String = "",
    var dateOfBirth: Date = Date(),
    var patientStatus: Boolean = false
)