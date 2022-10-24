package by.matthewvirus.medicinenotifier.ui.medicinesList

import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class MedicineListViewModel : ViewModel() {

    private val medicineRepository = MedicineRepository.get()
    val medicinesLiveData = medicineRepository.getMedicines()
}