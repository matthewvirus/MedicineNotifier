package by.matthewvirus.medicinenotifier.ui.allNotifications

import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class AllNotificationsViewModel : ViewModel() {
    
    private val medicineRepository = MedicineRepository.get()
    val medicineLiveData = medicineRepository.getMedicines()
}