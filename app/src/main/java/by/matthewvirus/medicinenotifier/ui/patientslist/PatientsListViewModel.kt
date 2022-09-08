package by.matthewvirus.medicinenotifier.ui.patientslist

import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.PatientDataModel

class PatientsListViewModel : ViewModel() {
    var patientsList = mutableListOf<PatientDataModel>()

    init {
        for (i in 0 until 20) {
            val patient = PatientDataModel()
            patient.patientName = "Patient"
            patient.patientSurname = "no. $i"
            patient.patientStatus = (i % 2 == 0)
            patient.patientDiagnosis = "Diagnosis no. $i"
            patientsList += patient
        }
    }
}