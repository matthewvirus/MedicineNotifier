package by.matthewvirus.medicinenotifier.ui.medicinesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import by.matthewvirus.medicinenotifier.data.domain.MedicineNotifierDatabase
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.data.domain.repository.MedicineRepository

class MedicineListViewModel(application: Application) : AndroidViewModel(application) {

    private val medicineRepository: MedicineRepository
    private val readAll: LiveData<List<Medicine>>

    init {
        val medicineDB = MedicineNotifierDatabase.getDatabase(application)
        medicineRepository = MedicineRepository(medicineDB.medicineDao())
        readAll = medicineRepository.getMedicines()
    }

    fun getMedicines(): LiveData<List<Medicine>> = readAll
}