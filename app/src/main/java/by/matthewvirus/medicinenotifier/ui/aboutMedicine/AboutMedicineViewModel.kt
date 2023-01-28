package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.Medicine
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class AboutMedicineViewModel : ViewModel() {

    private val medicineRepository = MedicineRepository.get()

    fun getMedicines(): LiveData<List<Medicine>> {
        return medicineRepository.getMedicines()
    }

    fun updateMedicine(medicine: Medicine) {
        medicineRepository.updateMedicine(medicine)
    }

    fun deleteMedicine(medicine: Medicine) {
        medicineRepository.deleteMedicine(medicine)
    }
}