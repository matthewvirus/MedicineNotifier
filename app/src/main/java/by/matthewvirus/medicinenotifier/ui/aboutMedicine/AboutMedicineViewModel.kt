package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository
import java.util.*

class AboutMedicineViewModel : ViewModel() {

    private val medicineRepository = MedicineRepository.get()

    fun getMedicine(id: Int): LiveData<MedicineDataModel?> {
        return medicineRepository.getMedicine(id)
    }

    fun updateMedicine(medicine: MedicineDataModel) {
        medicineRepository.updateMedicine(medicine)
    }

    fun deleteMedicine(medicine: MedicineDataModel) {
        medicineRepository.deleteMedicine(medicine)
    }
}