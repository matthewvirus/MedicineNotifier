package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import by.matthewvirus.medicinenotifier.data.database.MedicineNotifierDatabase
import by.matthewvirus.medicinenotifier.data.datamodel.Medicine
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AboutMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val medicineRepository: MedicineRepository
    private val readAll: LiveData<List<Medicine>>

    init {
        val medicineDB = MedicineNotifierDatabase.getDatabase(application)
        medicineRepository = MedicineRepository(medicineDB.medicineDao())
        readAll = medicineRepository.getMedicines()
    }

    fun updateMedicine(medicine: Medicine) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.updateMedicine(medicine)
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.deleteMedicine(medicine)
        }
    }

    fun getMedicines(): LiveData<List<Medicine>> = readAll
}