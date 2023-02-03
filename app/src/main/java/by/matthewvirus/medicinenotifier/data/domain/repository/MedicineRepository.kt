package by.matthewvirus.medicinenotifier.data.domain.repository

import androidx.lifecycle.LiveData
import by.matthewvirus.medicinenotifier.data.domain.dao.MedicineDao
import by.matthewvirus.medicinenotifier.data.model.Medicine

class MedicineRepository (private val medicineDao: MedicineDao) {

    fun getMedicines(): LiveData<List<Medicine>> =
        medicineDao.getMedicines()

    fun deleteMedicine(medicine: Medicine) =
        medicineDao.deleteMedicine(medicine)

    fun updateMedicine(medicine: Medicine) =
        medicineDao.updateMedicine(medicine)

    fun addMedicine(medicine: Medicine) =
        medicineDao.addMedicine(medicine)
}