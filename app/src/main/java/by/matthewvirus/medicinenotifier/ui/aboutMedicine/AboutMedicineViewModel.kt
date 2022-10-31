package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class AboutMedicineViewModel : ViewModel() {

    private val medicineRepository = MedicineRepository.get()

    fun getMedicines(): LiveData<List<MedicineDataModel>> {
        return medicineRepository.getMedicines()
    }

    fun updateMedicine(medicine: MedicineDataModel) {
        medicineRepository.updateMedicine(medicine)
    }

    fun deleteMedicine(medicine: MedicineDataModel) {
        medicineRepository.deleteMedicine(medicine)
    }
}