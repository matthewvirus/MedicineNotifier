package by.matthewvirus.medicinenotifier.ui.addpatient

import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class AddMedicineViewModel : ViewModel() {
    private val medicineRepository = MedicineRepository.get()
    fun addMedicine(medicine: MedicineDataModel) {
        medicineRepository.addMedicine(medicine)
    }
}