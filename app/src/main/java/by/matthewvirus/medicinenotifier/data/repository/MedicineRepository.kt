package by.matthewvirus.medicinenotifier.data.repository

import androidx.lifecycle.LiveData
import by.matthewvirus.medicinenotifier.data.database.dao.MedicineDao
import by.matthewvirus.medicinenotifier.data.datamodel.Medicine

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