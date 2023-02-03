package by.matthewvirus.medicinenotifier.ui.addMedicine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import by.matthewvirus.medicinenotifier.data.domain.MedicineNotifierDatabase
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.data.domain.repository.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val medicineRepository: MedicineRepository
    private val readAll: LiveData<List<Medicine>>

    init {
        val medicineDB = MedicineNotifierDatabase.getDatabase(application)
        medicineRepository = MedicineRepository(medicineDB.medicineDao())
        readAll = medicineRepository.getMedicines()
    }

    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch (Dispatchers.IO) {
            medicineRepository.addMedicine(medicine)
        }
    }

    fun getMedicines(): LiveData<List<Medicine>> = readAll
}